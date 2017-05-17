package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.os.Bundle;
import android.util.Log;

import java.io.File;

import blobDetection.Blob;
import blobDetection.BlobDetection;
import blobDetection.EdgeVertex;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Created by kkolyva on 06/02/2017.
 */

public class Detection extends PApplet {

    // INPUT:
    // OUTPUT:
    Solver d; // contans d.totalHoleNumber, result of the program
    public static long result;

    @Override
    public void settings() {
        // should run in the background mode now
        // fullScreen();
        size(1, 1);
    }

    public static boolean calculationDone = false;

    @Override
    public void setup() {
        background(255);

        String filePath = "img";

        // receive bundle with string path
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            filePath = bundle.getString("filePath");
        }

        int diameter = PelletMillSettings.dieDiameter.intValue();
        int parclength = PelletMillSettings.dieArcLength.intValue();

        d = new Solver(filePath, diameter, parclength);
        result = d.runCalculation();
        calculationDone = true;
    }

    @Override
    public void draw() {
        background(255);

        if(CalculationAcitivity.forceExit) {
            calculationDone = false;
            exit();
        }
    }


    public class Solver {
        // INPUT:  from user:
        PImage photo; // this photo will be processed
        String filePath; // path to the image in the device
        final long pelletMillDiameter; // radius of the pellet mill
        final long pelletMillArcLength;  // width of the pellet mill

        // From Developer:
        final float thresholdValue = 0.2f; // used in blob detection, may be, have to be adjusted for real world problems
        final float blurValue = 2; // defines how much you want to gett rid of the noise

        PGraphics processedPhoto; // used for output
        long imgHoleNumber;    // image number of holes
        final int screenRatio = 3; // distance beetween lines on the screen

        // OUTPUT:
        long totalHoleNumber; // total number of holes

        public Solver() {
            this.totalHoleNumber = 0;
            this.pelletMillDiameter = PelletMillSettings.dieDiameter.intValue();
            this.pelletMillArcLength = PelletMillSettings.dieArcLength.intValue();

            this.photo = createImage(10, 10, RGB);
            this.processedPhoto = createGraphics(10, 10);
        }

        // actual constructor
        public Solver(String filePath, int diameter, int arclength) {
            this.totalHoleNumber = 0;
            this.pelletMillDiameter = diameter;
            this.pelletMillArcLength = arclength;
            this.filePath = filePath;

            photo = loadImage(this.filePath);

            if (photo.width > photo.height) {
                photo.resize(0, 640);
            }
            else{
                photo.resize(640, 0);
            }

            // grab 1/3 of the screen that is inside of the rectangle
            if (photo.width > photo.height) {
                // rotate landscape image
                processedPhoto = createGraphics(photo.height, photo.width/screenRatio);
                processedPhoto.beginDraw();
                processedPhoto.background(255);
                processedPhoto.translate(processedPhoto.width/2, processedPhoto.height/2);
                processedPhoto.rotate(PI/2);
                processedPhoto.translate(-processedPhoto.height/2, -processedPhoto.width/2);
                processedPhoto.image(photo.get(photo.width/screenRatio, 0, photo.width/screenRatio, photo.height), 0, 0); // correct
                processedPhoto.endDraw();
            } else {
                processedPhoto = createGraphics(photo.width, photo.height/screenRatio);
                processedPhoto.beginDraw();
                processedPhoto.background(255);
                processedPhoto.image(photo.get(0, photo.height/screenRatio, photo.width, photo.height/screenRatio), 0, 0);
                processedPhoto.endDraw();
                //processedPhoto = photo.get(0, photo.height/screenRatio, photo.width, photo.height/screenRatio);
            }

        }

        /**
         * function calulates the number of holes for the whole pellet mill
         * this is the main algorithm
         */
        protected long runCalculation() {
            // apply necessary filters
            processedPhoto.filter(GRAY);
            processedPhoto.filter(BLUR, blurValue);
            processedPhoto.filter(THRESHOLD, thresholdValue);
            processedPhoto.filter(DILATE);
            processedPhoto.filter(ERODE);

            imgHoleNumber = calculateTotalNumberInImage(processedPhoto); //runDetection(pxHole);
            float arcLength = pelletMillArcLength;

            // Log.d("mmDiameter", "" + pelletMillDiameter);
            // Log.d("mmArcLength", "" + pelletMillArcLength);
            Log.d("imgHoleNumber", " " + imgHoleNumber);

            totalHoleNumber = (long) (PI * pelletMillDiameter / arcLength * imgHoleNumber);
            Log.d("totalHoleNumber", " " + totalHoleNumber);
            return totalHoleNumber;
        }

        //
        protected int calculateTotalNumberInImage(PImage photo1){
            BlobDetection bd = new BlobDetection(PApplet.parseInt(photo1.width), PApplet.parseInt(photo1.height));
            bd.setPosDiscrimination(false);
            bd.setBlobMaxNumber(3000);
            bd.setThreshold(thresholdValue);
            photo1.loadPixels();
            bd.computeBlobs(photo1.pixels);
            photo1.updatePixels();

            int toDrop = countBoundaryHoles(bd);
            Log.d("toDrop", " " + toDrop);
            return (bd.getBlobNb() - toDrop);
        }

        protected int countBoundaryHoles(BlobDetection bd){
            // figured out this value checking the values manually
            float eps = 1e-3f;

            int toDrop = 0;
            for (int b = 0; b < bd.getBlobNb(); ++b){
                if (bd.getBlob(b).x < eps)
                    toDrop++;
            }
            return toDrop;
        }

        /**
         * function starts the detection algorithm
         * as the result returns number of holes in the image.
         * the Blob detection library is used to solve the problem.
         * More sophistaced algorithms look unnecessary at this point.
         */
        protected long runDetection(float[] pxHole) {
            // convert image to grayscale, colors are redundant
            processedPhoto.filter(GRAY);
            // smoothen, to get rid of noise

            processedPhoto.filter(BLUR, blurValue);
//            processedPhoto.filter(THRESHOLD, thresholdValue);

//            processedPhoto.filter(DILATE);
//            processedPhoto.filter(ERODE);

            processedPhoto.save(new File(filePath).getAbsolutePath());


            // count holes in the given image
            long numberOfHoles = countHoles(pxHole);
            Log.d("numberOfHoles", " " + numberOfHoles);
            return numberOfHoles;
        }


        // performs blob detection
        protected int countHoles(float[] pxHole) {
            BlobDetection bd = new BlobDetection(PApplet.parseInt(processedPhoto.width), PApplet.parseInt(processedPhoto.height));
            bd.setPosDiscrimination(false);
            bd.setThreshold(thresholdValue);
            processedPhoto.loadPixels();
            bd.computeBlobs(processedPhoto.pixels);
            processedPhoto.updatePixels();

            // will be used for filtering out the small circles
            float pxHoleMean = getPxHoleMean(bd, processedPhoto.width, processedPhoto.height);
            float pxHoleMedian = getPxHoleMedian(bd, processedPhoto.width, processedPhoto.height);
            float pxHoleCenter =  getPxHoleCenter(bd, processedPhoto.width, processedPhoto.height);

            // assing the best filter here
            pxHole[0] = pxHoleMean;

            Log.d("pxHoleMean", " " + pxHoleMean);
            Log.d("pxHoleMedian", " " + pxHoleMedian);
            Log.d("pxHoleCenter", " " + pxHoleCenter);

            // filter out the values that are too small and too large
            int filteredHoles = filterOut(bd, processedPhoto.width, processedPhoto.height, pxHoleMean);
            Log.d("filteredHoles", " " + filteredHoles);
            return (bd.getBlobNb() - filteredHoles);
        }

        // used to count the holes that are half there
        protected int[] fixHalfHoles(BlobDetection bd, int pw, int ph, float mValue){
            //
            int [] numHalfHoles = new int []{0, 0};
            float halfMValue = mValue/2;
            float eps = mValue/pw; // how far from the borders the holes can be

            // there will be two columns of broken holes
            int firstCol = 0;
            int lastCol = 0;

            for (int j = 0; j < bd.getBlobNb(); ++j){
                // only width should be the case but we check both
                if (Math.abs(bd.getBlob(j).w*pw - halfMValue) < eps || Math.abs(bd.getBlob(j).h*ph - halfMValue) < eps){
                    // left or right boundary holes
                    if(bd.getBlob(j).x < eps){
                        firstCol++;
                    }
                    else if (bd.getBlob(j).x < (1.0 - eps)){
                        lastCol++;
                    }
                    else{
                        // not boundary hole
                    }
                }
            }
            numHalfHoles[0] = firstCol;
            numHalfHoles[1] = lastCol;
            return numHalfHoles;
        }

        // will be better to make a median filter here
        protected float getPxHoleMean(BlobDetection bd, int pw, int ph){
            float meanHole = 0;
            for (int j = 0; j < bd.getBlobNb(); ++j){
                meanHole += (bd.getBlob(j).w*pw +  bd.getBlob(j).h*ph)/2;
            }
            meanHole /= bd.getBlobNb();
            // println(meanHole);
            return meanHole;
        }

        /* filter out small blobs */
        // soft boundaries coeff
        protected int filterOut(BlobDetection bd, int pw, int ph, float mValue){
            int holesToDrop = 0;
            float coeff = 1.0f;
            for (int j = 0; j < bd.getBlobNb(); ++j){
                if ((Math.abs((bd.getBlob(j).w*pw)- mValue) > coeff*mValue) ||  (Math.abs((bd.getBlob(j).h*ph)- mValue) > mValue*coeff))
                    holesToDrop++;
            }
            return holesToDrop;
        }

        // median filter to detect the size of the holes in pixels
        protected float getPxHoleMedian(BlobDetection bd, int pw, int ph){
            int blobNb = bd.getBlobNb();

            float [] w = new float[blobNb];
            float [] h = new float[blobNb];
            for (int i = 0; i < blobNb; i++){
                w[i] = bd.getBlob(i).w;
                h[i] = bd.getBlob(i).h;
            }

            w = sort(w);
            h = sort(h);

            return  (w[blobNb/2]*pw + h[blobNb/2]*ph)/2;
        }

        // the central blob is considered to be the correct one
        protected float getPxHoleCenter(BlobDetection bd, int pw, int ph){
            float bestW = 0;
            float bestH = 0;
            float minDist = 1000;

            for (int j = 0; j < bd.getBlobNb(); ++j){
                float dist = dist(bd.getBlob(j).x, bd.getBlob(j).y, 0.5f, 0.5f); // pw/2, ph/2
                if (dist < minDist){
                    minDist = dist;
                    bestW = bd.getBlob(j).w;
                    bestH = bd.getBlob(j).h;
                }
            }

            return (bestW*pw + bestH*ph)/2;
        }

        // DEBUG: show images
        void drawBlobsAndEdges(BlobDetection theBlobDetection, PGraphics img, boolean drawBlobs, boolean drawEdges) {
            img.beginDraw();
            img.noFill();
            Blob b;
            EdgeVertex eA, eB;
            for (int n = 0; n < theBlobDetection.getBlobNb(); n++) {
                b = theBlobDetection.getBlob(n);
                if (b != null) {
                    // Edges
                    if (drawEdges) {
                        img.strokeWeight(2);
                        img.stroke(0, 255, 0);
                        for (int m = 0; m < b.getEdgeNb(); m++) {
                            eA = b.getEdgeVertexA(m);
                            eB = b.getEdgeVertexB(m);
                            if (eA != null && eB != null)
                                img.line(
                                        eA.x * img.width, eA.y * img.height,
                                        eB.x * img.width, eB.y * img.height
                                );
                        }
                    }

                    // Blobs
                    if (drawBlobs) {
                        img.strokeWeight(1);
                        img.stroke(255, 0, 0);
                        img.rect(
                                b.xMin * img.width, b.yMin * img.height,
                                b.w * img.width, b.h * img.height
                        );
                    }
                }
            }
        }
    }

}
