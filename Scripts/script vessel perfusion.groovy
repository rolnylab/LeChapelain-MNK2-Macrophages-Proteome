//Change channel names

setImageType('FLUORESCENCE');

setChannelNames('CD31', 'Lectin', 'DAPI')
//Change channel colors

setChannelColors(

getColorRGB(255, 0, 0),

getColorRGB(0, 255, 0),

)

//Set brightness and contrast

setChannelDisplayRange('CD31', 0, 2000)

setChannelDisplayRange('Lectin', 0, 1000)



//Add rectangle ROI (annotation)

import qupath.lib.roi.RectangleROI

import qupath.lib.objects.PathAnnotationObject

hierarchy = getCurrentHierarchy()

def imageData = getCurrentImageData()

def server = imageData.getServer()

def xdist = server.getWidth()

def ydist = server.getHeight()

clearAllObjects()

if (server.nZSlices() >0){

    0.upto(server.nZSlices()-1){

        frame = PathObjects.createAnnotationObject(ROIs.createRectangleROI(0,0,xdist,ydist,ImagePlane.getPlane(it,0)));

        addObject(frame);

    }

}

//Create vessel objects with existing pixel classifier: "Vessels"

selectAnnotations()

createDetectionsFromPixelClassifier("Vessels", 30.0, 10.0, "SPLIT")

//Adding measurements for single measurement classifier

selectDetections();

runPlugin('qupath.lib.algorithms.IntensityFeaturesPlugin', '{"pixelSizeMicrons":1.0,"region":"ROI","tileSizeMicrons":25.0,"channel1":true,"channel2":true,"channel3":true,"channel4":false,"doMean":true,"doStdDev":true,"doMinMax":true,"doMedian":true,"doHaralick":false,"haralickMin":NaN,"haralickMax":NaN,"haralickDistance":1,"haralickBins":32}')

addShapeMeasurements("AREA", "LENGTH", "MAX_DIAMETER", "MIN_DIAMETER")

selectAnnotations();

//Running single measurements for the exisiting object classifiers: "CD31" and "Lectin"

runObjectClassifier("CD31", "Lectin");


//Create vessel objects with existing pixel classifier: "Vessels"

createAnnotationsFromPixelClassifier("Vessels", 30.0, 10.0, "SPLIT")

selectAnnotations();

addPixelClassifierMeasurements("CD31", "CD31")

addPixelClassifierMeasurements("Lectin", "Lectin")



