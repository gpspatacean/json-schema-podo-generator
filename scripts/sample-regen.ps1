Write-Host "This will genererate samples. Please make sure this is rerun after each change that impacts generated code."

# Always make sure to delete everything first, in order to cater for cases in which source files are no longer generated
# with the added changes

$scriptFullPath = $MyInvocation.MyCommand.Path;
$generatorRootPath = Split-Path (Split-Path $scriptFullPath -Parent) -Parent
$jarLocation = Join-Path -Path $generatorRootPath -ChildPath "target\json-schema-podo-generator-0.0.1-SNAPSHOT.jar"
$schemaLocation = Join-Path -Path $generatorRootPath -ChildPath "samples\schemas\ComplexSchema.json"

##CPP area
Write-Host "Cleaning up samples/cpp"
Get-ChildItem (Join-Path -Path $generatorRootPath -ChildPath "samples/cpp") -Include *.* -Recurse -Exclude main.cpp,README.md | ForEach {
    $_.Delete()
}

$javaGeneratorArgs = " -jar " + $jarLocation + " generate -g cpp -i "+ $schemaLocation + " -o " + (Join-Path -Path $generatorRootPath -ChildPath "samples\cpp")
Write-Host "Regenerating samples/cpp"
Start-Process -NoNewWindow java -ArgumentList $javaGeneratorArgs

##Java area
Write-Host "Cleaning up samples/java/generated"
Get-ChildItem (Join-Path -Path $generatorRootPath -ChildPath "samples/java/generated") -Include *.* -Recurse | ForEach {
    $_.Delete()
}

$javaGeneratorArgs = " -jar " + $jarLocation + " generate -g java -i "+ $schemaLocation + " -o " + (Join-Path -Path $generatorRootPath -ChildPath "samples\java\generated")
Write-Host "Regenerating samples/java/generated"
Start-Process -NoNewWindow java -ArgumentList $javaGeneratorArgs