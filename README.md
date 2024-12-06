# MyWineApp
#### Video Demo:  https://youtu.be/wIJ8A69taEE
#### Description:
My final project for CS50 is a android app. I used Android Studio with jetpack compose and kotlin.
With my App, you can track your wine stock. It uses a sql-database to store information about your wine collection.
If the database is empty, the app prompts you to add a wine. At the insert-screen you type in the information and click the save button.
The button is only enabled if all necessary fields are filled. It also validates the correctness of the input.
Name has to be a string starting with a capital letter and year has to be 4-digit number.
The home-screen then displays all the entries in your database. By clicking the single wine you be redirected to the detail-screen.
There you can change the quantity of you wine with the “+” and “-” button or delete the whole entry.
At the home-screen you can also filter your wines. By filling the filterfield, the home-screen only displays the entries,
where the name of the wine is "like" the string in the filterfield.
