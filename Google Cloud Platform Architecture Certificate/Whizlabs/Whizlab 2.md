### 1. You can SSH into an instance from another instance in the same VPC by its internal IP address, but not its external IP address. What is one possible reason why this is so?

--> The firewall rule to allow SSH is restricted to the internal VPC  

> 인스턴스는 내부 및 외부 IP를 모두 가질 수 있다. firewall 가 ssh에 내부 VPC에서만 이용할 수 있도록 제한한 것이다.

### 2. Mountkirk Games needs to create a repeatable and configurable mechanism for deploying isolated application environments. Developers and testers can access each other's environments and resources, but they cannot access staging or production resources. The staging environment needs access to some services from production. What should you do to isolate development environments from staging and production?

--> Create a project for development and test and another for staging and production  
 
> 프로젝트를 서로 격리하고, 네트워크를 통한 세밀한 제어를 위해 Cross Project VPC나 Shared VPC를 사용하는 것이 이상적이다.  

### 3. The application reliability team at your company has added a debug feature to their backend service to send all server events to Google Cloud Storage for eventual analysis The event records are at least 50 KB and at most 15 MB and are expected to peak at 3,000 events per second. You want to minimize data loss. Which process should you implement?

--> Append metadata to file body. Compress individual files. name files with a random prefix pattern, save files to one bucket  

> uploading many files in parallel에서는 sequential filename( 타임스탬프 등 )을 쓰는 것을 지양해야한다.( Servername-eventsequence X )

### 4. Which of the following are characteristics of GCP VPC subnets? Choose 2 answers.

--> Each subnet can span at least 2 availability zones to provide a high-availability environment  
--> By default, all subnets can route between each other, whether they are private or public  

### 5. A  financial company has recently moved from  on premise to Google Cloud Platform , they have started to use Bigquery for data analysis , while the performance of Bigquery has been good ,but they are concerned about controlling the cost for Bigquery usage . Select the relevant Bigquery  best practises for controlling costs from the options given below.

--> Avoid SELECT * Query only the columns that you need.  
--> Use the --dry_run flag in the CLI Before running queries, preview them to estimate costs  
--> If possible, partition your Bigquery tables by date  

> Bigquery
