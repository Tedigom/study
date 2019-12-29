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


### 4.
