#!/bin/bash


get_system_metrics() {
    machine_name=$(hostname)
    total_physical_memory=$(awk '/MemTotal/ {printf "%.0f\n", $2}' /proc/meminfo)
    available_memory=$(awk '/MemAvailable/ {printf "%.0f\n", $2}' /proc/meminfo)
    cpu_usage=$(top -bn1 | grep "Cpu(s)" | sed "s/.*, *\([0-9.]*\)%* id.*/\1/" | awk '{print 100 - $1}')
    total_disk_size=$(df -B1 / | awk 'NR==2 {print $2}')
    free_disk_space=$(df -B1 / | awk 'NR==2 {print $4}')

initial_network_stats=$(cat /proc/net/dev | grep "eth0")
initial_bytes_received=$(echo "$initial_network_stats" | awk '{print $2}')
initial_bytes_transmitted=$(echo "$initial_network_stats" | awk '{print $10}')
sleep 1  # Wait for 1 second

# Get network statistics after 1 second
updated_network_stats=$(cat /proc/net/dev | grep "eth0")
updated_bytes_received=$(echo "$updated_network_stats" | awk '{print $2}')
updated_bytes_transmitted=$(echo "$updated_network_stats" | awk '{print $10}')

# Calculate instantaneous speeds
in_speed=$((updated_bytes_received - initial_bytes_received))
out_speed=$((updated_bytes_transmitted - initial_bytes_transmitted))


    echo "machineName: $machine_name"
    echo "totalPhysicalMemory: $total_physical_memory"
    echo "availablePhysicalMemory: $available_memory"
    echo "cpuUtilization: $cpu_usage"
    echo "totalDiskSize: $total_disk_size"
    echo "freeDiskSpace: $free_disk_space"
    echo "networkIn: $in_speed"
    echo "networkOut: $out_speed"
}


    get_system_metrics
