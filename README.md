# Silver_eureka_shopify
This is an Android application where you can start your shopping journey

**Features** 

Authentication
- User authentication using Firebase including user registration and and login.
- User can reset password to the given email if forgets somehow.
- The user can log out whenever he wants.

Application Functionality
- The application is divided into four main parts
  1. Products
     - Products are fetched using the Retrofit and shown on the main fragment along with multiple tab layouts giving option multiple category options.
     - Users can also see all the product details and add them to their cart.
  2. Search
     - User can search products with categories and name
  3. Cart
     - Products to the cart are being added using Firestore and user can also increase or decrease their quantities depending on their liking.
     - Also you can check out multiple items at once.
  4. User Information
     - You can see user information along with image, name, mobile, and email.
     - You can see all the user orders list and the status of a particular order.
     - Users can also add multiple addresses at once and can select one with the address to deliver.
    
**Technologies Used**
- Kotlin
- RetroFit for fetching API.
- Hilt for dependency Injection.
- MVVM pattern.
- Kotlin coroutines along with Kotlin flows.
- Firebase authentication.
- Firebase Firestore for handling data.
- Glide library for images.
- Android navigation


**Future Enhancement**
- There are a lot of features that are yet to be implemented.
1. Payment Gateway
2. Login using OTP, Facebook, and Google sign-in.
3. Able to like or dislike the products.





