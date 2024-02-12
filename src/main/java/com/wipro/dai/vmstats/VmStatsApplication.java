package com.wipro.dai.vmstats;

import com.wipro.dai.vmstats.exception.VirtualMachineNotFoundException;
import com.wipro.dai.vmstats.model.VMStatsData;
import com.wipro.dai.vmstats.service.UsageDetailService;
import com.wipro.dai.vmstats.service.VMStatsService;
import com.wipro.dai.vmstats.service.VirtualMachineServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class VmStatsApplication implements CommandLineRunner {

	@Autowired
	private VMStatsService vmStatsService;
	@Autowired
	private VirtualMachineServiceImpl virtualMachineService;

	@Autowired
	private UsageDetailService usageDetailService;
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
//
//
		virtualMachineService.registerMachine();
	}

	@Scheduled(fixedRate = 300000)
	public void updateVmStatsPeriodically() {
		try {
			log.info("Updating VM stats. Start time: {}",  LocalDateTime.now());
			VMStatsData vmstats = vmStatsService.getVMStats();
			log.info("VM stats updated: {}", vmstats);
			usageDetailService.updateVMStats( vmstats);
		} catch (VirtualMachineNotFoundException | InterruptedException | IOException | URISyntaxException e) {
			log.error( e.getMessage());
			// Optionally, you can log the stack trace as well
		}
	}
}
