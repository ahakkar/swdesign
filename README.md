# PROJECT NORDICWATT
Group assignment for Course COMP.SE.110 Software Design
Project Members:

- Antti Hakkarainen
- Markus Hissa
- Heikki Hohtari
- Janne Taskinen

### Demo of the prototype
You can view demo of the prototype by following this [link](https://tuni-my.sharepoint.com/personal/janne_taskinen_tuni_fi/_layouts/15/stream.aspx?id=%2Fpersonal%2Fjanne%5Ftaskinen%5Ftuni%5Ffi%2FDocuments%2FSoftware%20Design%2FRyhm%C3%A4kansio%2F2023%2D09%2D29%2020%2D40%2D13%2Emp4&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0RpcmVjdCJ9fQ&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0RpcmVjdCJ9fQ&ga=1).

To test this prototype yourself read section "How to test the program" on this readme file.

### Design document
The latest version of the design document can be found from projects root directory "DesignDocument.docx"

## How to test the program

### Prerequisites:

- Java Runtime Environment (JRE) 11 or newer
- .env file with following contents:
  - FINGRID=E54NatJoP14O81oHjpF1R3y4typLrLAL2cfYVdqW`

### Option 1: Using an IDE

1. Clone the repository
2. Add the .env file to the project root directory (see Prerequisites)
2. Open the project in an IDE (e.g. IntelliJ IDEA)
3. Build and run the program

### Option 2: Using the built JAR-file
1. Clone the repository
2. Find the latest .jar file from {project root directory}/artifacts/Java_jar
2. Create a .env file to the same directory as the .jar file (see Prerequisites)
3. In {project root directory}/artifacts/Java_jar, run the command `java -jar Java.jar`


