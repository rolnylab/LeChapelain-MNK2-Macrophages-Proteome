setImageType('FLUORESCENCE');

clearAllObjects();

setChannelNames('PIMO', 'DAPI')
createFullImageAnnotation(true)

createAnnotationsFromPixelClassifier("Hypoxic region", 0.0, 0.0)
createAnnotationsFromPixelClassifier("DAPI", 0.0, 0.0)

addPixelClassifierMeasurements("Hypoxic region", "Hypoxic region")
addPixelClassifierMeasurements("DAPI", "DAPI")
