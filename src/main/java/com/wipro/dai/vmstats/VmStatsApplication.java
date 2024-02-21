package com.wipro.dai.vmstats;

import com.wipro.dai.vmstats.service.UsageDetailService;
import com.wipro.dai.vmstats.service.VMStatsService;
import com.wipro.dai.vmstats.service.VirtualMachineServiceImpl;
import com.wipro.dai.vmstats.service.iics.IICSApiActivities;
import com.wipro.dai.vmstats.service.iics.IICSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VmStatsApplication implements CommandLineRunner {

	@Autowired
	private VMStatsService vmStatsService;
	@Autowired
	private VirtualMachineServiceImpl virtualMachineService;


	@Autowired
	private UsageDetailService usageDetailService;
	@Autowired
	private IICSApiActivities iicsApiActivities;

	@Autowired
	private IICSService iicsService;


	final Logger log = LoggerFactory.getLogger(VmStatsApplication.class);

	public static void main(String[] args) {
//		SpringApplication.run(VmStatsApplication.class, args);


		SpringApplication app = new SpringApplication(VmStatsApplication.class);
		app.setRegisterShutdownHook(false); // Disable the shutdown hook
		app.run(args);


	}

	@Override
	public void run(String... args) throws Exception {

//		log.info("Updating the VM table...");
//		VirtualMachine vm1 = new VirtualMachine(null, "Divya", "AWS", "us-east-1", "Linux", LocalDateTime.now(), LocalDateTime.now() );
//		VirtualMachine vm2 = new VirtualMachine(null, "raspberrypi", "AWS", "us-east-1", "Linux", LocalDateTime.now(), LocalDateTime.now() );
//		VirtualMachine vm3 = new VirtualMachine(null, "MyVM3", "AWS", "us-east-1", "Linux", LocalDateTime.now(), LocalDateTime.now() );
//
//
//		virtualMachineService.createVirtualMachine(vm1);
//		virtualMachineService.createVirtualMachine(vm2);
//		virtualMachineService.createVirtualMachine(vm3);
//
//		virtualMachineService.registerMachine();
//		iicsService.updateActivityLog();
//		System.out.println(iicsApiActivities.DownloadActivityLog("https://usw1.dmp-us.informaticacloud.com/saas","jx4FMcoAAKjlps7DUl4lgv",10));

		iicsService.updateActivityLog();
		try {
			iicsService.updateMeterUsage();
		}catch (Exception e){
			log.error(e.getMessage());
		}
		System.out.println("Done...");

	}



//	@Scheduled(fixedRate = 10000)
//	public void updateVmStatsPeriodically() {
//		try {
//			log.info("Updating VM stats. Start time: {}",  LocalDateTime.now());
//			VMStatsData vmstats = vmStatsService.getVMStats();
//			usageDetailService.updateVMStats( vmstats);
//			log.info("VM stats updated: {}", vmstats);
//		} catch (VirtualMachineNotFoundException | InterruptedException |
//                 IOException | URISyntaxException e) {
//			log.error( e.getMessage());
//
//		}
//	}


//	@Scheduled(cron = "0 0 12 * * ?")
//		@Scheduled(fixedRate = 10000)
//	public void updateIICSMeterUsagePeriodically() {
//		try {
//			log.info("Updating IICS meter usage. Start time: {}",  LocalDateTime.now());
//			iicsService.updateMeterUsage();
//
//		} catch (Exception e) {
//			log.error( e.getMessage());
//
//		}
//	}



}
