import os
import shutil

def on_post_build(config):
    potential_folders = [
        'modules/clock-time-common/build/docs/javadoc',
        'modules/clock-time-paper/build/docs/javadoc',
        'build/docs/javadoc'
    ]
    
    source_folder = None
    for folder in potential_folders:
        if os.path.exists(folder):
            source_folder = folder
            break
            
    target_folder = os.path.join(config['site_dir'], 'apidocs')
    
    if source_folder:
        if os.path.exists(target_folder):
            shutil.rmtree(target_folder)
        shutil.copytree(source_folder, target_folder)
        print(f"Copied Javadoc from {source_folder} to {target_folder}")
    else:
        print("Warning: No Javadoc folder found.")
