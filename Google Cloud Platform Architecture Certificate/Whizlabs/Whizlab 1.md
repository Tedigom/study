### 1. For this question, refer to the TerramEarth case study.  https://cloud.google.com/certification/guides/cloud-architect/casestudy-terramearth-rev2
### Because you do not know every possible future use for the data TerramEarth collects, you have decided to build a system that captures and stores all raw data in case you need it later. How can you most cost-effectively accomplish this goal?

A. Have the vehicles in the field stream the data directly into BigQuery.  
B. Have the vehicles in the field pass the data to Cloud Pub/Sub and dump it into a Cloud Dataproc cluster that stores data in Apache Hadoop Distributed File System (HDFS) on persistent disks.  
C. Have the vehicles in the field continue to dump data via FTP, adjust the existing Linux machines, and use a collector to upload them into Cloud Dataproc HDFS for storage.  
D. Have the vehicles in the field continue to dump data via FTP, and adjust the existing Linux machines to immediately upload it to Cloud Storage with gsutil.  


### 2. For this question, refer to the TerramEarth case study.  https://cloud.google.com/certification/guides/cloud-architect/casestudy-terramearth-rev2
### Today, TerramEarth maintenance workers receive interactive performance graphs for the last 24 hours (86,400 events) by plugging their maintenance tablets into the vehicle. The support group wants support technicians to view this data remotely to help troubleshoot problems. You want to minimize the latency of graph loads. How should you provide this functionality?

A. Execute queries against data stored in a Cloud SQL.  
B. Execute queries against data indexed by vehicle_id.timestamp in Cloud Bigtable.  
C. Execute queries against data stored on daily partitioned BigQuery tables.  
D. Execute queries against BigQuery with data stored in Cloud Storage via BigQuery federation.  

### 3. For this question, refer to the TerramEarth case study.  https://cloud.google.com/certification/guides/cloud-architect/casestudy-terramearth-rev2
### Your agricultural division is experimenting with fully autonomous vehicles. You want your architecture to promote strong security during vehicle operation. Which two architecture characteristics should you consider?


A. Use multiple connectivity subsystems for redundancy.  
B. Require IPv6 for connectivity to ensure a secure address space.  
C. Enclose the vehicle’s drive electronics in a Faraday cage to isolate chips.  
D. Use a functional programming language to isolate code execution cycles.  
E. Treat every microservice call between modules on the vehicle as untrusted.  
F. Use a Trusted Platform Module (TPM) and verify firmware and binaries on boot.  

### 4. For this question, refer to the TerramEarth case study.  https://cloud.google.com/certification/guides/cloud-architect/casestudy-terramearth-rev2
### Which of TerramEarth’s legacy enterprise processes will experience significant change as a result of increased Google Cloud Platform adoption?

A. OpEx/CapEx allocation, LAN change management, capacity planning  
B. Capacity planning, TCO calculations, OpEx/CapEx allocation  
C. Capacity planning, utilization measurement, data center expansion  
D. Data center expansion,TCO calculations, utilization measurement  

### 5. For this question, refer to the TerramEarth case study.  https://cloud.google.com/certification/guides/cloud-architect/casestudy-terramearth-rev2
### You analyzed TerramEarth’s business requirement to reduce downtime and found that they can achieve a majority of time saving by reducing customers’ wait time for parts. You decided to focus on reduction of the 3 weeks’ aggregate reporting time. Which modifications to the company’s processes should you recommend?

A. Migrate from CSV to binary format, migrate from FTP to SFTP transport, and develop machine learning analysis of metrics.  
B. Migrate from FTP to streaming transport, migrate from CSV to binary format, and develop machine learning analysis of metrics.  
C. Increase fleet cellular connectivity to 80%, migrate from FTP to streaming transport, and develop machine learning analysis of metrics.  
D. Migrate from FTP to SFTP transport, develop machine learning analysis of metrics, and increase dealer local inventory by a fixed factor.  

### 6. Your company wants to deploy several microservices to help their system handle elastic loads. Each microservice uses a different version of software libraries. You want to enable their developers to keep their development environment in sync with the various production services. Which technology should you choose?

A. RPM/DEB  
B. Containers  
C. Chef/Puppet  
D. Virtual machines  

### 7. Your company wants to track whether someone is present in a meeting room reserved for a scheduled meeting. There are 1000 meeting rooms across 5 offices on 3 continents. Each room is equipped with a motion sensor that reports its status every second. You want to support the data upload and collection needs of this sensor network. The receiving infrastructure needs to account for the possibility that the devices may have inconsistent connectivity. Which solution should you design?

A. Have each device create a persistent connection to a Compute Engine instance and write messages to a custom application.  
B. Have devices poll for connectivity to Cloud SQL and insert the latest messages on a regular interval to a device specific table.  
C. Have devices poll for connectivity to Cloud Pub/Sub and publish the latest messages on a regular interval to a shared topic for all devices.  
D. Have devices create a persistent connection to an App Engine application fronted by Cloud Endpoints, which ingest messages and write them to Cloud Datastore.  

### 8. Your company wants to try out the cloud with low risk. They want to archive approximately 100 TB of their log data to the cloud and test the analytics features available to them there, while also retaining that data as a long-term disaster recovery backup. Which two steps should they take?

A. Load logs into BigQuery.  
B. Load logs into Cloud SQL.  
C. Import logs into Stackdriver.  
D. Insert logs into Cloud Bigtable.  
E. Upload log files into Cloud Storage.  

### 9. You set up an autoscaling instance group to serve web traffic for an upcoming launch. After configuring the instance group as a backend service to an HTTP(S) load balancer, you notice that virtual machine (VM) instances are being terminated and re-launched every minute. The instances do not have a public IP address. You have verified that the appropriate web response is coming from each instance using the curl command. You want to ensure that the backend is configured correctly. What should you do?

A. Ensure that a firewall rule exists to allow source traffic on HTTP/HTTPS to reach the load balancer.  
B. Assign a public IP to each instance, and configure a firewall rule to allow the load balancer to reach the instance public IP.  
C. Ensure that a firewall rule exists to allow load balancer health checks to reach the instances in the instance group.  
D. Create a tag on each instance with the name of the load balancer. Configure a firewall rule with the name of the load balancer as the source and the instance tag as the destination.  

### 10. Your organization has a 3-tier web application deployed in the same network on Google Cloud Platform. Each tier (web, API, and database) scales independently of the others. Network traffic should flow through the web to the API tier, and then on to the database tier. Traffic should not flow between the web and the database tier. How should you configure the network?

A. Add each tier to a different subnetwork.  
B. Set up software-based firewalls on individual VMs.  
C. Add tags to each tier and set up routes to allow the desired traffic flow.  
D. Add tags to each tier and set up firewall rules to allow the desired traffic flow.  

### 11. Your organization has 5 TB of private data on premises. You need to migrate the data to Cloud Storage. You want to maximize the data transfer speed. How should you migrate the data?

A. Use gsutil.  
B. Use gcloud.  
C. Use GCS REST API.  
D. Use Storage Transfer Service.  

### 12. You are designing a mobile chat application. You want to ensure that people cannot spoof chat messages by proving that a message was sent by a specific user. What should you do?

A. Encrypt the message client-side using block-based encryption with a shared key.  
B. Tag messages client-side with the originating user identifier and the destination user.  
C. Use a trusted certificate authority to enable SSL connectivity between the client application and the server.  
D. Use public key infrastructure (PKI) to encrypt the message client-side using the originating user’s private key.  

### 13. You are designing a large distributed application with 30 microservices. Each of your distributed microservices needs to connect to a database backend. You want to store the credentials securely. Where should you store the credentials?

A. In the source code  
B. In an environment variable  
C. In a key management system  
D. In a config file that has restricted access through ACLs  

### 14. For this question, refer to the Mountkirk Games case study. https://cloud.google.com/certification/guides/cloud-architect/casestudy-mountkirkgames-rev2
### Mountkirk Games wants to set up a real-time analytics platform for their new game. The new platform must meet their technical requirements. Which combination of Google technologies will meet all of their requirements?

A. Kubernetes Engine, Cloud Pub/Sub, and Cloud SQL  
B. Cloud Dataflow, Cloud Storage, Cloud Pub/Sub, and BigQuery  
C. Cloud SQL, Cloud Storage, Cloud Pub/Sub, and Cloud Dataflow  
D. Cloud Pub/Sub, Compute Engine, Cloud Storage, and Cloud Dataproc  

### 15. For this question, refer to the Mountkirk Games case study. https://cloud.google.com/certification/guides/cloud-architect/casestudy-mountkirkgames-rev2
### Mountkirk Games has deployed their new backend on Google Cloud Platform (GCP). You want to create a thorough testing process for new versions of the backend before they are released to the public. You want the testing environment to scale in an economical way. How should you design the process?

A. Create a scalable environment in GCP for simulating production load.  
B. Use the existing infrastructure to test the GCP-based backend at scale.  
C. Build stress tests into each component of your application and use resources from the already deployed production backend to simulate load.  
D. Create a set of static environments in GCP to test different levels of load—for example, high, medium, and low.  

### 16. Your customer is moving their corporate applications to Google Cloud Platform. The security team wants detailed visibility of all resources in the organization. You use Resource Manager to set yourself up as the org admin. What Cloud Identity and Access Management (Cloud IAM) roles should you give to the security team?

A. Org viewer, Project owner  
B. Org viewer, Project viewer  
C. Org admin, Project browser  
D. Project owner, Network admin  

### 17. To reduce costs, the Director of Engineering has required all developers to move their development infrastructure resources from on-premises virtual machines (VMs) to Google Cloud Platform. These resources go through multiple start/stop events during the day and require state to persist. You have been asked to design the process of running a development environment in Google Cloud while providing cost visibility to the finance department. Which two steps should you take?

A. Use persistent disks to store the state. Start and stop the VM as needed.  
B. Use the --auto-delete flag on all persistent disks before stopping the VM.  
C. Apply VM CPU utilization label and include it in the BigQuery billing export.  
D. Use BigQuery billing export and labels to relate cost to groups.  
E. Store all state in local SSD, snapshot the persistent disks, and terminate the VM.  
F. Store all state in Cloud Storage, snapshot the persistent disks, and terminate the VM.  

### 18. Your company has decided to make a major revision of their API in order to create better experiences for their developers. They need to keep the old version of the API available and deployable, while allowing new customers and testers to try out the new API. They want to keep the same SSL and DNS records in place to serve both APIs. What should they do?

A. Configure a new load balancer for the new version of the API.  
B. Reconfigure old clients to use a new endpoint for the new API.  
C. Have the old API forward traffic to the new API based on the path.  
D. Use separate backend services for each API path behind the load balancer.  

### 19. The database administration team has asked you to help them improve the performance of their new database server running on Compute Engine. The database is used for importing and normalizing the company’s performance statistics. It is built with MySQL running on Debian Linux. They have an n1-standard-8 virtual machine with 80 GB of SSD zonal persistent disk. What should they change to get better performance from this system in a cost-effective manner?

A. Increase the virtual machine’s memory to 64 GB.  
B. Create a new virtual machine running PostgreSQL.  
C. Dynamically resize the SSD persistent disk to 500 GB.  
D. Migrate their performance metrics warehouse to BigQuery.  

https://docs.google.com/forms/d/e/1FAIpQLSdvf8Xq6m0kvyIoysdr8WZYCG32WHENStftiHTSdtW4ad2-0w/viewscore?viewscore=AE0zAgBie1F_ffyIZCGBJk6k6cDRVKnaPYFxdqXvfoOJDilrMKrHQiALZp1fB5B8KWTCd24
