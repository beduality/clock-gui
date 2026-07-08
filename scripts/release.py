#!/usr/bin/env python3
import os
import re
import sys
import subprocess
from datetime import date
from pathlib import Path
from cyclopts import App
from rich.console import Console
from rich.prompt import Prompt, Confirm

console = Console()
app = App()

def get_current_version() -> str:
    path = Path("gradle.properties")
    if not path.exists():
        raise FileNotFoundError("gradle.properties not found")
    content = path.read_text()
    match = re.search(r"^version\s*=\s*([^\s]+)", content, re.MULTILINE)
    if not match:
        raise ValueError("Could not find version in gradle.properties")
    return match.group(1).strip()

def bump_version(current: str, bump_type: str) -> str:
    bump_type_clean = bump_type.strip().lower()
    
    # Check if the bump_type is a specific custom version
    if re.match(r"^\d+\.\d+\.\d+$", bump_type_clean):
        return bump_type_clean
        
    match = re.match(r"^(\d+)\.(\d+)\.(\d+)(.*)$", current)
    if not match:
        raise ValueError(f"Current version '{current}' is not a valid semver")
    
    major, minor, patch, suffix = match.groups()
    major, minor, patch = int(major), int(minor), int(patch)
    
    if bump_type_clean == "major":
        major += 1
        minor = 0
        patch = 0
    elif bump_type_clean == "minor":
        minor += 1
        patch = 0
    elif bump_type_clean == "patch":
        patch += 1
    else:
        raise ValueError(
            f"Invalid bump type: '{bump_type}'. Choose 'major', 'minor', 'patch' or a specific version X.Y.Z"
        )
        
    return f"{major}.{minor}.{patch}{suffix}"

def update_gradle_properties(new_version: str):
    path = Path("gradle.properties")
    content = path.read_text()
    new_content = re.sub(
        r"^version\s*=\s*.*$", f"version = {new_version}", content, flags=re.MULTILINE
    )
    path.write_text(new_content)
    console.print("[green]✔[/green] Updated gradle.properties")

def update_pyproject_toml(new_version: str):
    path = Path("pyproject.toml")
    content = path.read_text()
    new_content = re.sub(
        r"^version\s*=\s*\".*\"$", f"version = \"{new_version}\"", content, flags=re.MULTILINE
    )
    path.write_text(new_content)
    console.print("[green]✔[/green] Updated pyproject.toml")

def update_changelog(new_version: str):
    path = Path("CHANGELOG.md")
    content = path.read_text()
    today = date.today().isoformat()
    
    if "## [Unreleased]" not in content:
        raise ValueError("Could not find '## [Unreleased]' section in CHANGELOG.md")
    
    new_header = f"## [{new_version}] - {today}"
    new_content = content.replace("## [Unreleased]", new_header, 1)
    path.write_text(new_content)
    console.print("[green]✔[/green] Updated CHANGELOG.md")

def run_command(cmd: list[str], env: dict = None):
    console.print(f"[bold blue]Running:[/bold blue] {' '.join(cmd)}")
    result = subprocess.run(cmd, env=env)
    if result.returncode != 0:
        console.print(f"[bold red]Command failed with exit status {result.returncode}[/bold red]")
        sys.exit(result.returncode)

@app.default
def main(
    bump: str = None,
    no_dry_run: bool = False,
    no_push: bool = False,
):
    """Automate the release process by bumping versions, updating changelogs, dry-running publications, and tagging.

    Parameters
    ----------
    bump : str, optional
        The type of version bump ('major', 'minor', 'patch') or a specific version string.
        If omitted, starts wizard mode.
    no_dry_run : bool, optional
        Skip the dry-run publish verification task.
    no_push : bool, optional
        Skip committing, tagging, and pushing changes to git.
    """
    try:
        current_version = get_current_version()
    except Exception as e:
        console.print(f"[bold red]Error reading current version:[/bold red] {e}")
        sys.exit(1)
        
    console.print(f"Current version: [bold cyan]{current_version}[/bold cyan]")
    
    # Wizard Mode
    if bump is None:
        console.print("\n[bold yellow]--- Wizard Mode ---[/bold yellow]")
        choices = ["patch", "minor", "major", "custom"]
        choice = Prompt.ask(
            "Select bump type",
            choices=choices,
            default="patch",
        )
        if choice == "custom":
            bump = Prompt.ask("Enter custom version (e.g., 0.5.0)")
        else:
            bump = choice
            
    try:
        new_version = bump_version(current_version, bump)
    except Exception as e:
        console.print(f"[bold red]Error calculating new version:[/bold red] {e}")
        sys.exit(1)
        
    console.print(f"Target release version: [bold green]{new_version}[/bold green]")
    
    if not Confirm.ask("Do you want to proceed with the release?"):
        console.print("[yellow]Release aborted.[/yellow]")
        sys.exit(0)
        
    # 1. Update Version Numbers and Changelog
    console.print("\n[bold]1. Updating version numbers & changelog...[/bold]")
    try:
        update_gradle_properties(new_version)
        update_pyproject_toml(new_version)
        update_changelog(new_version)
    except Exception as e:
        console.print(f"[bold red]Error updating files:[/bold red] {e}")
        sys.exit(1)
        
    # 2. Run uv lock to update lock file
    console.print("\n[bold]2. Updating uv.lock...[/bold]")
    run_command(["uv", "lock"])
    
    # 3. Dry-Run Verification
    if not no_dry_run:
        console.print("\n[bold]3. Dry-Run Verification...[/bold]")
        if Confirm.ask("Do you want to run dry-run publication verification?", default=True):
            env = os.environ.copy()
            env["DRY_RUN"] = "true"
            env["JAVA_HOME"] = "/usr/lib/jvm/java-21-openjdk"
            run_command(
                [
                    "./gradlew",
                    "publishPluginPublicationToHangar",
                    "modrinth",
                    "--no-daemon",
                ],
                env=env,
            )
            
    # 4. Commit and Push Tag
    if not no_push:
        console.print("\n[bold]4. Git Tag & Push...[/bold]")
        if Confirm.ask(f"Commit, tag as v{new_version}, and push to remote?", default=True):
            run_command(["git", "add", "gradle.properties", "pyproject.toml", "CHANGELOG.md", "uv.lock"])
            run_command(["git", "commit", "-m", f"chore: release version {new_version}"])
            run_command(["git", "tag", f"v{new_version}"])
            run_command(["git", "push", "origin", "main", "--tags"])
            console.print(f"\n[bold green]Successfully released v{new_version}![/bold green]")

if __name__ == "__main__":
    app()
