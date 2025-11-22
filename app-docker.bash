# Build the CDS-optimized image
docker build -t bookstore-demo-cds .

# Run the optimized container
docker run -p 8080:8080 bookstore-demo-cds

# Run with custom memory settings
docker run -p 8080:8080 -e JAVA_OPTS="-Xmx512m -XX:MaxRAMPercentage=75.0 -XX:SharedArchiveFile=application.jsa" bookstore-demo-cds
