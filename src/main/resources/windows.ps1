

function Get-SystemMetrics {
    $computerSystem = Get-CimInstance -ClassName Win32_ComputerSystem
    $totalPhysicalMemoryInBytes = $computerSystem.TotalPhysicalMemory
    $totalPhysicalMemoryInMB = [math]::Round($totalPhysicalMemoryInBytes / 1MB, 2)
	
	$operatingSystem = Get-CimInstance -ClassName Win32_OperatingSystem
    $availableMemoryInBytes = $operatingSystem.FreePhysicalMemory*1024



    $cpuUsageCounter = (Get-Counter '\Processor(_Total)\% Processor Time').CounterSamples
    $cpuUsage = [math]::Round($cpuUsageCounter.CookedValue, 2)
	
	$diskDrives = Get-CimInstance -ClassName Win32_DiskDrive | Measure-Object -Property Size -Sum
    $totalDiskSizeInBytes = $diskDrives.Sum
  


    $freeDiskSpaceCounter = (Get-Counter '\LogicalDisk(_Total)\Free Megabytes').CounterSamples
    $freeDiskSpace = $freeDiskSpaceCounter.CookedValue
	$freeDiskSpaceInBytes = $freeDiskSpace * 1024 * 1024

    $networkInCounter = (Get-Counter '\Network Interface(*)\Bytes Received/sec').CounterSamples
    $networkOutCounter = (Get-Counter '\Network Interface(*)\Bytes Sent/sec').CounterSamples

    $networkIn = ($networkInCounter | Measure-Object -Property CookedValue -Sum).Sum
    $networkOut = ($networkOutCounter | Measure-Object -Property CookedValue -Sum).Sum



    return @{
        "MachineName" = hostname
        "TotalPhysicalMemory" = $totalPhysicalMemoryInBytes
		"AvailableMemory" = $availableMemoryInBytes
        "CpuUsage" = $cpuUsage
        "TotalDiskSize" = $totalDiskSizeInBytes
        "FreeDiskSpace" = $freeDiskSpaceInBytes
        "NetworkIn" = $networkIn
        "NetworkOut" = $networkOut
    }
}

# Log system metrics every 5 seconds

    $systemMetrics = Get-SystemMetrics

    Write-Host "machineName: $($systemMetrics.MachineName)"
    Write-Host "totalPhysicalMemory: $($systemMetrics.TotalPhysicalMemory)"
	Write-Host "availablePhysicalMemory: $($systemMetrics.AvailableMemory)"
    Write-Host "cpuUtilization: $($systemMetrics.CpuUsage)"
    Write-Host "totalDiskSize: $($systemMetrics.TotalDiskSize)"
    Write-Host "freeDiskSpace: $($systemMetrics.FreeDiskSpace)"
    Write-Host "networkIn: $($systemMetrics.NetworkIn)"
    Write-Host "networkOut: $($systemMetrics.NetworkOut)"


