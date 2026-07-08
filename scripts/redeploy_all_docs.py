import os
import subprocess
from pathlib import Path

root = Path("/home/luis/GitHub/beduality/clock-time")

versions = {
    "v0.1.0": ("0.1.0", None),
    "v0.2.0": ("0.2.0", None),
    "v0.3.0": ("0.3.0", None),
    "v0.4.0": ("0.4.0", "latest"),
    "main": ("unreleased", "Unreleased")
}

def run(cmd, env=None):
    print(f"Running: {' '.join(cmd)}")
    subprocess.run(cmd, cwd=root, env=env, check=True)

try:
    for tag, (version_name, alias) in versions.items():
        print(f"\n==================== PROCESSING {tag} ====================")
        run(["git", "reset", "--hard"])
        run(["git", "checkout", tag])
        run(["git", "clean", "-fdx", "-e", ".venv", "-e", ".gradle", "-e", ".env"])
        run(["git", "checkout", "main", "--", "mkdocs.yml", "pyproject.toml", "uv.lock", "docs/overrides/", "docs/hooks/"])
        
        # Build Javadocs
        env = os.environ.copy()
        env["JAVA_HOME"] = "/usr/lib/jvm/java-21-openjdk"
        run(["./gradlew", "clean", "javadoc", "--no-daemon"], env=env)
        
        # Deploy with mike
        if tag == "main":
            run(["uv", "run", "mike", "deploy", "-t", "Unreleased", "unreleased"])
        else:
            if alias:
                run(["uv", "run", "mike", "deploy", version_name, alias])
            else:
                run(["uv", "run", "mike", "deploy", version_name])
                
    run(["uv", "run", "mike", "set-default", "latest"])
    
finally:
    run(["git", "reset", "--hard"])
    run(["git", "checkout", "main"])
