import qupath.lib.gui.tools.MeasurementExporter

//
// CELL SEGMENTATION AND CLASSIFICATION
//
// Set image type and get image handles


   

def imageData = getCurrentImageData()
//getCurrentViewer().setImageData(imageData)

setImageType('FLUORESCENCE');
setChannelNames('F480', 'NG2', 'CD31', 'DAPI')

// Clear all objects before starting
clearAllObjects();

createFullImageAnnotation(true)
for (annotation in getAnnotationObjects()) {
    annotation.setPathClass(getPathClass("Cells"))
    annotation.setName("Cells")
}


// Segment nuclei
runPlugin('qupath.imagej.detect.cells.WatershedCellDetection', '{"detectionImage":"NG2","requestedPixelSizeMicrons":0.5,"backgroundRadiusMicrons":8.0,"backgroundByReconstruction":true,"medianRadiusMicrons":0.0,"sigmaMicrons":1.5,"minAreaMicrons":30.0,"maxAreaMicrons":400.0,"threshold":100.0,"watershedPostProcess":false,"cellExpansionMicrons":0.1,"includeNuclei":false,"smoothBoundaries":true,"makeMeasurements":true}')
selectDetections();
runObjectClassifier("CD31");


//Create vessel objects with existing pixel classifier: "Vessels"
// VESSEL SEGMENTATION
//
// Segment vessels
createFullImageAnnotation(true)
VesselAnnotation = getAnnotationObjects().findAll{it.getName() == null}[0]
VesselAnnotation.setName("Vessels")
VesselAnnotation.setPathClass(getPathClass("Vessel"))

createDetectionsFromPixelClassifier("Vessels", 20.0, 10.0, "SPLIT")

//
// SAVE RESULTS
//
// Export measurements table
name = GeneralTools.stripExtension(getCurrentImageName()).split("-")[0]
mkdirs(buildFilePath(PROJECT_BASE_DIR, "results"))
def outputPath = buildFilePath(PROJECT_BASE_DIR, "results", name+"_measurements")
def outputFile = new File(outputPath)

saveDetectionMeasurements(outputPath + ".csv")

// Export Cells contours
selectDetections()
exportSelectedObjectsToGeoJson(outputPath + ".json", "PRETTY_JSON", "FEATURE_COLLECTION")

