# Use an official Android runtime as a parent image
FROM openjdk:11-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the APK file into the container
COPY volvo-demo /app/volvo-demo

# Install necessary tools
RUN apt-get update && apt-get install -y wget unzip

# Set up Android SDK
ENV ANDROID_HOME=/usr/local/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Download and install Android SDK
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-8092744_latest.zip -O android-sdk.zip && \
    unzip -q android-sdk.zip -d ${ANDROID_HOME}/cmdline-tools && \
    rm android-sdk.zip && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses --sdk_root=${ANDROID_HOME} && \
    ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager "platform-tools" --sdk_root=${ANDROID_HOME}

# Command to run the app (this is just a placeholder, as we can't actually run the app in a container)

#CMD ["echo", "Android app containerized successfully"]

CMD ["tail", "-f", "/dev/null"]