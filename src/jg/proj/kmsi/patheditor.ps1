# Credit to https://stackoverflow.com/a/37304698 for the code!
$new_entry = $args[0]

$old_path = [Environment]::GetEnvironmentVariable('path', 'machine');
$new_path = $old_path + ';' + $new_entry
[Environment]::SetEnvironmentVariable('path', $new_path,'Machine');