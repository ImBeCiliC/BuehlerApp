## BuehlerApp

The BuehlerApp is a freelance project made by [Klim](https://github.com/milkyklim) and me.

## Project Description

Bühler Group was planning to develop a cloud based photo app for the service personnel in the feed milling industries to enhance efficiency and ensure quality of our customer maintenance service.

Unexperienced service personnel is calculating or estimating a wrong amount of holes in the pellet mill dies when doing the maintenance of the pellet mills on customer site. The **amount of holes** is crucial for the output and wrong information can have serious impacts for the customers.

Our **task** was to make the service personnel’s life easier and ensure quality by developing a photo app. The app should automatically calculate the amount of holes in a pellet mill die using a photo of the die, specified diameter and arc length.

## Roles

- [Klim](https://github.com/milkyklim) developed the algorithm, implemented it in [Processing](https://www.processing.org) and embedded the code into the Android application. He was communicating with the project manager from the Bühler Group.

- I was responsible for the Android application development, debugging and testing.

The design suggestions were provided by Bühler Group.

## Algorithm Description

After multiple ideas I decided to go with the simplest (reliability and implementation time) possible solution. The app had to process the photo on the phone without sending the data to the cloud.

Therefore, we use the following algorithm which involves only thresholding and morphological operations.

- Take a picture.
- Take a central third of the image to speedup the calculations.
- Threshold the part of the image (since the pictures are taken under the same conditions the threshold value can be constant).
- Apply morphological operations – dilate and erode – to get rid of the detached pixels.
- Find all blobs in the image.
- Check that the blobs have reasonable size – not too small (big) for the wholes.
- Count the number of all holes in the pellet mill die using the formula:

$$ N_{total} = \frac{2 \pi}{l} \cdot N_{arc} \text{, where} $$

$l$ is the length of the arc provided by user;
$N_{arc}$ is the number of wholes corresponding to the given arc length.

## Application Description

### Workflow

The main steps (screens):

- Splash screen with the Bühler logo.
- Input screen: user specifies the die number, width and arc length.
- Info screen: user can have a look at the suggestions for the input data.
- Camera screen: user takes the picture of the pellet mill die so that it fits in the given region.
- Computing screen: the animation is shown during computations.
- Final screen: the result of the calculations is shown.

![App-Design|small](https://c1.staticflickr.com/3/2942/34081065246_4c58d3b644_b.jpg)

<!-- ![App-Icon](https://c1.staticflickr.com/3/2827/34081064976_eb99c8c307_m.jpg) -->

### Splash screen
<img src="https://c1.staticflickr.com/3/2843/33736613820_016aa2c1a2_b.jpg" width="427" height="720"/>

### Input screen
<div style="float: left;">
  <img src="https://c1.staticflickr.com/3/2819/33279124484_4d86b3881b_b.jpg" width="427" height="720"/>

  <img src="https://c1.staticflickr.com/3/2913/33736613940_7f171c4115_b.jpg" width="427" height="720" />

  <img src="https://c1.staticflickr.com/3/2882/34081064496_4e1398b147_b.jpg" width="427" height="720" />

</div>

### Info screen
<img src="https://c1.staticflickr.com/4/3948/33279124454_b79e433b66_b.jpg" width="427" height="720" />


### Camera screen
<div style="float: left;">
  <img src="https://c1.staticflickr.com/3/2821/33736614330_ec154431f3_b.jpg" width="427" height="720" />
  <img src="https://c1.staticflickr.com/3/2941/33279125764_c933ee14d6_b.jpg" width="427" height="720" />
</div>

### Computing screen
<img src="https://c1.staticflickr.com/3/2941/33279124784_d3035d1c37_b.jpg" width="427" height="720" />

### Result screen
<img src="https://c1.staticflickr.com/3/2870/33736613990_4452469e48_b.jpg" width="427" height="720" />
