# TrenulMeu-Mobile
Android Mobile App for www.trenulmeu.ro

# What is TrenulMeu?
TrenulMeu is a long running Project designed to provide the very first modern, easy to use and complete solution for Rail travelers in the form of a great service across the Web and Mobile.
The Project started around 2 years ago and had seen many improvements. For this Event I created the Mobile Client, partner for any on the go! It is using the Public facing RESTful API provided by TrenulMeu.ro.

# The Basics:
I use Open Data to get the routes from all the Rail Companies from Romania.
I also use my own algorithms to make the DataSet more "Europe-like" (introduction of different Train Types, adding some International connections, etc.)
A proprietary Distributed, Scaled and very Efficient Algorithm is used to generate all Routes between any 2 Stations in the Network. Then entire Romanian network can be computed in less than 5 minutes! White paper: https://onedrive.live.com/redir?resid=DCD1811F0039E57F!9183&authkey=!AMR-Uk7Xv586tGM&ithint=file%2cpdf
Routes are then stored in a efficient BlobStorage System in Azure.
Here, www.trenulmeu.ro seves a front-end interface and the RESTful API.
And were comes the Mobile Application...

# What does it do?
The Mobile Application uses the data collected by storing it for offline use in an embedded Database being less than 2 MB in size. This allows for almost all operations using the Application to be executed without the need of an Internet connection!
The only 2 features that are not available are Routing and Delay checking.
Other than that, the Mobile Application allows for complex searching across the Network with impressive flexibility, granularity and speed.

# Notice:
TrenulMeu, as in the Algorithm, Website and Mobile App, is not available to the general public as they are still in development.