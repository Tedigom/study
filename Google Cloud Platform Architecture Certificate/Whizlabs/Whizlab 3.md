### 1. You have created a Kubernetes engine cluster named 'mycluster'. You've realized that you need to change the machine type for the cluster from n1-standard-1 to n1-standard-4. What is the command to make this change?
--> You must create a new node pool in the same cluster and migrate the workload to the new pool  

> node pool을 만들고 난 다음에는 machine type를 바꿀 수 없다.

### 2. Your marketing department wants to send out a promotional email campaign. The development team wants to minimize direct operation management. They project a wide range of possible customer responses, from 100 to 500,000 click-throughs per day. The link leads to a simple website that explains the promotion and collects user information and preferences. Which infrastructure should you recommend?

--> Use Google App Engine to serve the website and Google Cloud Datastore to store user data.  

> 100 ~ 500,000클릭은 simple한 어플리케이션이므로, Managed Instance Group과 Cloud bigtable을 쓰는 것은 오버스펙이다. GAE와 Datastore를 쓰는것이 적당하다.  

### 3. Using principal of least privilege and allowing for maximum automation, what steps can you take to store audit logs for long-term access and to allow access for external auditors to view? Choose the 2 correct answers:

--> Export audit logs to Cloud Storage via an export sink.  
--> Create account for auditors to have view access to export storage bucket with the Storage Object Viewer role.  

> Stackdriver Logging 권한을 주는 것은 "Long-term access"의 관점에서 맞지 않은 보기이다.


### 4. Dress4Win wants to do penetration security scanning on the test and development environments deployed to the cloud. The scanning should be performed from an end user perspective as much as possible. How should they conduct the penetration testing?

--> Use the on-premises scanners to conduct penetration testing on the cloud environments routing traffic over the public internet  

> 클라우드 환경에 security scanner를 배포하면, border 보안 구성을 테스트하기 힘들다. 따라서 scanner를 외부(onpremise 등)에 위치시키고, public internet을 이용하여 테스트 하는 것이 end-user 입장에서 사용하는 것과 비슷하게 테스트 할 수 있다.  

### 5. You are migrating your existing data center environment to Google Cloud Platform. You have a 1 petabyte Storage Area Network (SAN) that needs to be migrated. What GCP service will this data map to?

--> Persistent Disk  
> SAN데이터는 block storage를 사용하며, 이는 persistent disk로 대체될 수 있다. SAN 대신 NAS로 작업하는 경우, Persistent Disk와 Cloud Storage에 맵핑될 수 있다.

### 6. As part of their new application experience, Dress4Win allows customers to upload images of themselves. The customer has exclusive control over who may view these images.Customers should be able to upload images with minimal latency and also be shown their images quickly on the main application page when they log in. Which configuration should Dress4Win use?  

--> Store image files in a google cloud storage bucket. Use Google cloud datastore to maintain metadata that maps each customer's ID and their image files  

> block storage가 아니라면, Google cloud storage bucket을 쓰는 것을 적극 고려해야한다. distributed file system 보다 GCS가 더 성능이 좋을것.

### 7. Your company runs several databases on a single MySQL instance. They need to take backups of a specific database at regular intervals. The backup activity needs to complete as quickly as possible and cannot be allowed to impact disk performance. How should you configure the storage?

--> Mount a Local SSD volume as the backup location. After the backup is complete, use gsutil to move the backup to Google Cloud Storage. 
