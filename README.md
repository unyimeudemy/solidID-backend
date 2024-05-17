# SOLID ID (Backend)

## ABOUT
In summary, the project is proposed solution for identity theft. It is a centralized identity verification platform 
that serves a single source for the identity of individual and organizations .

## MOTIVATION
The motivation for this project came after checking some statistics on identity theft.
Did you know that every 3 seconds there is a victim of identity theft in the United States?
did you know that over $29 billion was stolen by identity thieves in 2022?

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

This repository is the backend built with spring boot. You can check out the client repository in
[SolidID-client](https://github.com/unyimeudemy/solidID-client) which is built with reactjs.

Here communication with database is done via Java Persistence API (JPA). The relation database engine used here is postgres, and it is 
deployed in aws Relation Database Service (RSD) and images are stored in aws Simple Storage Service (S3 bucket). 

- Click https://solidid-client.onrender.com/ to check out the live project which is deployed on render.com
- All essential functionalities are unit tested and to run unit test use `./mvnw test`
- To build project run `./mvnw verify clean`