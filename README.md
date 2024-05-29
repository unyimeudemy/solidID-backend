# SOLID ID (Backend)

## ABOUT
In summary, the project is proposed solution for identity theft. It is a centralized identity verification platform 
that serves a single source for the identity of individual and organizations .

## MOTIVATION
The motivation for this project came after checking some statistics on identity theft.
Did you know that every **<span style="color: red;">3 seconds</span>**  there is a victim of identity theft in the United States?
And did you know that over **<span style="color: red;">$29 billion</span>** was stolen by identity thieves in 2022?

Actually, there are solutions out there but all of them seem to focus on rectification after
the damage as already been done. This is because no one can totally stop people's data from getting
into the wrong hands, but my solution aims to render it useless.

## HOW IT WORKS
If an individual or organization wants to verify the identity of a persons, the person will generate a 
one-time use verification token that the other party can use to fetch his/her details.

Users that are part of an organization can choose the appropriate profile and the other party will very 
if he/she is part of on an organization without seeing the users personal details.

Each time the identity of a user is used, a record of when and who did the 
verification is kept. 

## TECHNOLOGIES

<a>
    <img height="400px" src="https://solididbucket.s3.amazonaws.com/photos/Model+databases.png" alt="Jenkins logo"> 
</a>

This repository is the backend, built with spring boot. You can check out the client repository in
[SolidID-client](https://github.com/unyimeudemy/solidID-client) which is built with reactjs.

Here communication with database is done via Java Persistence API (JPA). The relation database engine used here is postgres, and it is 
deployed in aws Relation Database Service (RSD) and images are stored in aws Simple Storage Service (S3 bucket). 

- Click https://solidid-client.onrender.com/ to check out the live project which is deployed on render.com
- All essential functionalities are unit tested and to run unit test use `./mvnw test`
- To build project run `./mvnw verify clean`
- The app is currently using the CI/CD service from render.com

## WORK FLOW
To use the software, Sign up a new user or sign in with the details below 
- Email : unyime1@gmail.com
- Password : 123456

_OVERVIEW_
When a user is logged in, the user can go to the profile section, select a profile and generate a token 
which can be used for verification. Note that the profile chosen is the what will be displayed to a third party 
that is using the token. Also note the token generated is a one-time token.

On the profile page, logged-in users can also view their identity usage history. This history contains all
time that the current user's account was verified and by whom. The page also has a button that opens up a
form for adding a new organization.
For a user to add an organization, the organization must be already registered with the platform and the user
added as member. 

To register an organization, click the signup button on the home page, and you will be redirected to the
signup page for users. Then click the link at the bottom of the page to be redirected to the signup page
organizations. For quick access as an organization, use the credentials below
- Email : org2@gmail.com
- Password : 123456

On the organization's profile page, there is a button to get all the members of the organization and 
another to add a new member.

