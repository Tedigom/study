### 1. Operational parameters such as oil pressure are adjustable on each of TerramEarth's vehicles to increase their efficiency, depending on their environmental conditions. Your primary goal is to increase the operating efficiency of all 20 million cellular and unconnected vehicles in the field. How can you accomplish this goal?
 
 --> Capture all operating data, train machine learning models that identify ideal operations, and run locally to make operational adjustments automatically.  
 
 > Terram earth의 2000만대의 차량에서 데이터는 로컬로 저장이 되기 때문에, Google ML Platform을 쓰는 것보다, locally run 하는 것이 효율적이다.
 
 ### 2. You need to regularly create disk level backups of the root disk of a critical instance. These backups need to be able to be converted into new instances that can be used in different projects. How should you do this? Select 2 possible way to accomplish this
 
 --> Create Snapshots, turn the snapshot into a custom image, and share the image across projects  
 --> create snapshots and share them to other projects  
 
 > stream Vm's data를 Cloud Storage에 넣고, 다른 project에 Cloud Storage export를 하는 것은 
 'regularly create disk level backups'를 하는 것을 만족시키지 못한다.
 
 ### 3. Your company has decided to build a backup replica of their on- premises user authentication PostgreSQL database on Google Cloud Platform. The database is 4 TB, and large updates are frequent. Replication requires RFC1918  private address space. Which networking approach would be the best choice?
 
 --> Google Cloud Dedicated Interconnect or Google Cloud Partner Interconnect  

> 나는 Create two vpn Tunnels within the same cloud VPN gateway to the same destination VPN Gateway를 선택했다. 하지만 동일한 클라우드 VPN 게이트웨이 내에서 동일한 대상 VPN 게이트웨이에 대해 두 개의 VPN 터널을 만들 수 없으므로 답이 아니다.

### 4. You are using DataFlow to ingest a large amount of data and later you send the data to Bigquery for Analysis, but you realize the data is dirty, what would be the best choice to use to clean the data in stream with serverless approach?

--> Fetch the data from Bigquery and create one more pipeline, clean data from DataFlow and send it back to Bigquery  

> DataPrep에서는 특정 데이터만을 전처리할 수 있으므로 옳지 않다.Data flow에서는 Data cleaning process에서 customize 할 수 있다.  

### 5. You have a long-running job that one of your employees has permissions to start. You don’t want that job to be terminated when the employee who last started that job leaves the company. What would be the best way to address the concern in this scenario?

--> Create a service account.Grant the Service Account User to your employees who needs permission to start the job. Grant the Compute Instance Admin to the same employees.  

> 해당 서비스에 필요한 권한만으로 각 서비스에 대한 서비스 계정을 만드는 것이 최선의 방법이다.

 
