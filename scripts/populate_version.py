import json
import subprocess

def get_total_commits():
    # Get the total commits as a raw string
    v = subprocess.check_output(['git', 'rev-list', '--all', '--count'])
    
    # Process it
    v = v.decode('utf-8')
    v = v.strip()

    v = int(v)

    return v

PROP_PATH = 'gradle.properties'

# Get properties
f = open(PROP_PATH, 'r')
PROP = f.read()
f.close()

def get_version_section():
    proc = PROP
    ver_token = '\tmod_version = '
    
    i = proc.find(ver_token) + len(ver_token)
    j = PROP.find('\n', i)

    return [i, j]

def get_cur_version():
    [i, j] = get_version_section()

    return PROP[i:j]

def calc_next_version():
    ver = get_cur_version()

    # Split
    ver = ver.split('.')

    # Replace last part
    ver[-1] = str(get_total_commits())

    return '.'.join(ver)

def update_version(new_ver):
    # Get version part
    [i, j] = get_version_section()

    # Copy string
    proc = PROP

    # Replace part of string
    proc = proc[0:i] + str(new_ver) + proc[j:]

    return proc

# Save new properties
f = open(PROP_PATH, 'w')

# Calculate the next version number
next_ver = calc_next_version()

# Generate new properties
new_prop = update_version(next_ver)


# Write the changes
f.write(new_prop)

# Close stream
f.close()