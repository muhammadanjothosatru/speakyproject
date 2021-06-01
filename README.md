# SPEAKY PROJECT
Speaky is a self learning public speaking machine learning based. The machine learning is used to assess disfluency, eye gazing, and eye blinking. After that the model deployed in google cloud platform with cloud functions and integrated with firebase. Android application trigger send mfcc to storage and trigger API to pass input to the model. After that, the result is sent to application again. 

# Machine Learning

# Android

# Cloud
- Use Google Cloud Platform as foundational architecture.
- Create a project in Google Cloud Platform.
- Add team member and manage roles.
- Create storage bucket that close enough to our market service. In our case it's in asia-southeast2 (Jakarta). It is a regional bucket so the service can serve fast enough.
- Create firebase project that integrated to Google Cloud Platform Project. Firebase is used to make API and Database.
- Create cloud functions to trigger API with HTTP Request. We choose cloud functions because removes the work of managing servers, configuring software, updating frameworks, and     patching operating systems. The software and infrastructure are fully managed by Google so that you just add code. Furthermore, provisioning of resources happens automatically     in response to events.
- Upload all model to Cloud Storage. The model is saved in model.json format because it can easily trigger with API.
- Edit the index.js so the API can work. The principle works is that the API receives a trigger from Android and then takes the link and input token that has been uploaded to       storage. After getting the link and token, the API sends it to model.json to predict. After getting the predict results, then send it to Android to be displayed to the user.
- After the index.js complete. Testing the API with POSTMAN API.
- Deploy index.js in cloud functions. We use asia-southeast2 as location and 8 GiB memory allocation.
