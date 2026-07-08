import os
import shutil

def on_post_build(config):
    # Javadoc is built in modules/clock-time-common/build/docs/javadoc
    # We want to copy it to site_dir/apidocs
    source_folder = 'modules/clock-time-common/build/docs/javadoc'
    target_folder = os.path.join(config['site_dir'], 'apidocs')
    
    if os.path.exists(source_folder):
        if os.path.exists(target_folder):
            shutil.rmtree(target_folder)
        shutil.copytree(source_folder, target_folder)
        print(f"Copied Javadoc from {source_folder} to {target_folder}")
    else:
        print(f"Warning: Javadoc folder not found at {source_folder}")
