FROM python:3.12-slim AS builder

WORKDIR /build

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    ffmpeg \
    git \
    gnupg \
    maven \
    nodejs \
    npm \
    openjdk-21-jdk \
    tesseract-ocr \
    tesseract-ocr-ara \
    tesseract-ocr-ces \
    tesseract-ocr-chi-sim \
    tesseract-ocr-chi-tra \
    tesseract-ocr-dan \
    tesseract-ocr-deu \
    tesseract-ocr-fin \
    tesseract-ocr-fra \
    tesseract-ocr-heb \
    tesseract-ocr-hin \
    tesseract-ocr-hun \
    tesseract-ocr-ita \
    tesseract-ocr-jpn \
    tesseract-ocr-kor \
    tesseract-ocr-lav \
    tesseract-ocr-nld \
    tesseract-ocr-nor \
    tesseract-ocr-pol \
    tesseract-ocr-por \
    tesseract-ocr-rus \
    tesseract-ocr-spa \
    tesseract-ocr-swe \
    tesseract-ocr-tha \
    tesseract-ocr-tur \
    tesseract-ocr-ukr \
    tesseract-ocr-vie \
    tesseract-ocr-sqi \
    tzdata \
    wget \
    && npm install -g grunt-cli \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

COPY pom.xml ./
COPY docs-core/pom.xml docs-core/pom.xml
COPY docs-web-common/pom.xml docs-web-common/pom.xml
COPY docs-web/pom.xml docs-web/pom.xml
COPY config config
COPY docs-core docs-core
COPY docs-web-common docs-web-common
COPY docs-web docs-web

RUN mvn -Pprod -DskipTests clean package -pl docs-web -am \
    && cp docs-web/target/*.war /tmp/docs.war

FROM python:3.12-slim

LABEL maintainer="b.gamard@sismics.com"

ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/ \
    JAVA_OPTIONS="-Dfile.encoding=UTF-8 -Xmx1g" \
    JETTY_VERSION=11.0.20 \
    JETTY_HOME=/opt/jetty

RUN apt-get update && \
    apt-get -y -q --no-install-recommends install \
    ca-certificates \
    less \
    mediainfo \
    openjdk-21-jre-headless \
    procps \
    tzdata \
    unzip \
    vim \
    wget \
    ffmpeg \
    tesseract-ocr \
    tesseract-ocr-ara \
    tesseract-ocr-ces \
    tesseract-ocr-chi-sim \
    tesseract-ocr-chi-tra \
    tesseract-ocr-dan \
    tesseract-ocr-deu \
    tesseract-ocr-fin \
    tesseract-ocr-fra \
    tesseract-ocr-heb \
    tesseract-ocr-hin \
    tesseract-ocr-hun \
    tesseract-ocr-ita \
    tesseract-ocr-jpn \
    tesseract-ocr-kor \
    tesseract-ocr-lav \
    tesseract-ocr-nld \
    tesseract-ocr-nor \
    tesseract-ocr-pol \
    tesseract-ocr-por \
    tesseract-ocr-rus \
    tesseract-ocr-spa \
    tesseract-ocr-swe \
    tesseract-ocr-tha \
    tesseract-ocr-tur \
    tesseract-ocr-ukr \
    tesseract-ocr-vie \
    tesseract-ocr-sqi \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN dpkg-reconfigure -f noninteractive tzdata

RUN wget -nv -O /tmp/jetty.tar.gz \
    "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/${JETTY_VERSION}/jetty-home-${JETTY_VERSION}.tar.gz" \
    && tar xzf /tmp/jetty.tar.gz -C /opt \
    && mv /opt/jetty* /opt/jetty \
    && useradd jetty -U -s /bin/false \
    && chown -R jetty:jetty /opt/jetty \
    && mkdir -p /opt/jetty/webapps /data \
    && chown -R jetty:jetty /data \
    && chmod +x /opt/jetty/bin/jetty.sh

COPY docs.xml /opt/jetty/webapps/docs.xml
COPY --from=builder /tmp/docs.war /opt/jetty/webapps/docs.war

WORKDIR /opt/jetty

EXPOSE 8080

USER jetty

CMD ["java", "-jar", "/opt/jetty/start.jar"]
