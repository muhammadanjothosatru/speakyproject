# SPEAKY PROJECT
Speaky is a self learning public speaking machine learning based. The machine learning is used to assess disfluency, eye gazing, and eye blinking. After that the model deployed in google cloud platform with cloud functions and integrated with firebase. Android application trigger send mfcc to storage and trigger API to pass input to the model. After that, the result is sent to application again. 

## Machine Learning
- Make datasets or find public datasets for disfluent speech, eye blink, and eye gaze. 
- For the disfluent speech dataset, we use xxxxxxxxxxxxx
- For the eye blink dataset, we use a public dataset from MRL Eye Dataset as shown in ``Dataset info.md``
- For the eye gaze dataset, we use datasets from a conference paper entitled “EYE CONTROLLED VIRTUAL KEYBOARD USING CONVOLUTIONAL NEURAL NETWORKS” as shown in the ``Dataset       info.md``
- Make model for speech disfluency, eye blink, and eye gaze detection
- Save and export the existing model to .h5 file format
- Deploy the existing model using Flask 

## Android

## Cloud
- Use Google Cloud Platform as foundational architecture.
- Create a project in Google Cloud Platform.
- Add team members and manage roles.
- Create a storage bucket that is close enough to our market service. In our case it's in asia-southeast2 (Jakarta). It is a regional bucket so the service can serve fast enough.
- Create Cloud Computing Architecture for web server. We choose cloud computing service because we can freely design the architecture.
- Create Flask to make API work for deployed model on Virtual Machine.
- Testing the API.
- Deploy flask in virtual machine

