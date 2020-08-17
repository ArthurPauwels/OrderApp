# OrderApp
Assignment for the Native Apps I: Android August exam.
This app is a POC of an idea I might want to improve upon in the future. (though, it would make more sense as a webapp first)

## Screen-recording
A screen-recording of the app can be found in te ./screenrecording directory. It's about 2 minutes and was very awkward to record. Since I don't have access to a modern android device I rand my code in the emulator.

## The idea
About a year ago, me and a friend were having dinner. The place we were eating at happened to have an iPad at the counter where you could order food and drinks. At the time we fantasised about how nice it would be if the app running on the iPad ran on our phones. That way we would both have easy access to the menu and we could both take our own time to order (and even pay separately)
Fast-forward to the recent pandemic, and businesses have taken to sticking QR codes on the tables that directs customers to a site that hosts their menu. Upon noticing this I thought It would be great if that link also allowed you to order and pay since you could embed the table information in the QR code / link.

## The POC Android app
I wanted to try implementing the basics that would also show what I've learned during the android courses:
- scanning a QR code to grab the restaurant and table information
- an option to enter the code manually
- selecting the items you want to order
- a way to see your order history

## Running the app - instructions
Before running, it's best to first start up the api. This can be found in the RestoAPI. zip folder. You can run it by running 'node server.js'. Take note of your personal ip address and the port it's running on, because you will need to replace the baseUrl constant in the BusinessAPIService.kt file. I was initially going to try to host the API somewhere online but I didn't find a suitable way to do so in time.

Once the app is running, you can scan this code. It corresponds to a sushi restaurant called Makisu.

![Qr code](https://github.com/ArthurPauwels/OrderApp/blob/master/qrcode.png?raw=true)
