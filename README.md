# CodingChallenge
To calculate the total number of passengers that each
transport type is able to mobilize on each file, in order to have partials that could be
easily summarized later. 

### Pre-requisite
* AWS-SAM (https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
* Localstack (https://github.com/localstack/localstack)
* AWS CLI (https://docs.aws.amazon.com/cli/latest/userguide/installing.html)
* Docker
* Java8
* Maven

### How to run the project?
#### Step 1
Configuration change to connect the aws locally.
```
git clone https://github.com/SudarsonS/CodingChallenge.git
cd CodingChallenge/
vi src/main/resources/application.properties
```
Change `aws_service_end_point` to the local ip address. (For eg.,) In MacOS, `System Preferences > Network > IP address`
(P.s.) The Localstack will be running in docker. It is necessary to give the ip address instead of `localhost`.

#### Step 2
Create the jar with Maven package.
```
cd CodingChallenge/
mvn clean package
```
#### Step 3
Start the Localstack for mocking the aws services.
```
localstack start --docker
```
#### Step 4
Generic AWS commands used in the project.

```
Create Bucket              => aws --endpoint-url=http://localhost:4572 s3 mb s3://bonial-transport/
List Bucket                => aws --endpoint-url=http://localhost:4572 s3 ls s3://bonial-transport/
Copy file from local to s3 => aws --endpoint-url=http://localhost:4572 s3 cp file_name s3://bonial-transport/records/1/
Copy file from s3 to local => aws --endpoint-url=http://localhost:4572 s3 cp s3://bonial-transport/records/1/file_name .
```
After starting the Localstack, follow the below steps:
* Create the bucket => `aws --endpoint-url=http://localhost:4572 s3 mb s3://bonial-transport/`
* Copy the file (data.json) to the bucket, which contains the sample data => `aws --endpoint-url=http://localhost:4572 s3 cp data.json s3://bonial-transport/records/1/`
* Generate the put event in s3 using aws-sam, which invokes the lambda function => `sam local generate-event s3 put --bucket bonial-transport --key records/1/data.json | sam local invoke "TransportHandlerFunction"`
* Copy the summary file locally to see the result => `aws --endpoint-url=http://localhost:4572 s3 cp s3://bonial-transport/summary/1/result .`

#### Result

{"CAR":14,"TRAIN":150,"FLIGHT":24}

### For different partners
In order to create the summary for different partners.
#### Step 1
Change the partner id in the application.properties
```
cd CodingChallenge/
vi src/main/resources/application.properties
```
Change the `result_key` to different partner id. (For eg), `summary/2/result`

(P.s) Partner id for simplicity hard-coded in the application.properties.

#### Step 2
Make changes to the key when copying the data.json to the s3. (For e.g) aws --endpoint-url=http://localhost:4572 s3 cp data.json s3://bonial-transport/records/`2`/
