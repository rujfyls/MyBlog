insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'You’ve probably grown weary of me talking about Spring Native, but I can’t help myself: it’s awesome! GraalVM is a drop-in replacement for OpenJDK that includes an extra tool called native-image, which you can add after installing the GraalVM distribution. The native-image tool is an ahead-of-time (AOT) compiler that turns your .class files into architecture-specific machine code. It’s a native image. Which means you lose the benefits of portability. Which isn’t great. But, and there is a big but, here: the resulting binary is self-contained, takes minimal RAM at runtime, and starts up at 10x or more the speed of the equivalent JRE-bound application. A self-contained binary is a great thing because it means you can package it into a tiny operating system footprint, ideal for distribution in a Docker image. It takes minimal RAM (or, more specifically, RSS), requiring very little memory at runtime. This is also ideal for distribution in a Docker image and on a container orchestrator like Kubernetes because it means that you can deploy many times more instances of the application with the same resources that would’ve been required to run the application on the JRE. And, finally, it starts up fast. Real fast. My Spring Boot applications routinely startup anywhere from 20-75ms, depending on what the application does. For example, you can imagine how useful this would be in a serverless context. There are some other benefits to using GraalVM as well. Did you know that you could use GraalVM to turn your application into a linked library? Like a .dll, .dylib, or .so? That means you could link other applications to the functionality offered by your Spring Boot code. Check out this most recent Spring Tips (@SpringTipsLive) video I did introducing Spring Native 0.11.x. You can get started right now by going to the Spring Initializr (start.spring.io) and choosing Spring Native.', '01/12/2020', 'Spring Native and GraalVM', 0, 2);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'In 2012, Facebook tried to figure out how to walk the microservices walk when building their clients. There’s a natural tension there: clients want all the related data in one fell swoop, while the services want to be separate and modular. So, how could both sides get what they wanted? They created GraphQL to provide a way to query an API and get as much or as little of the data as requested. You could build the API in terms of a graph of services, but the client needn’t know that. They didn’t have to care about the network calls required to resolve payload requests of a given shape. That was all hidden behind the API. Facebook open-sourced GraphQL in 2015, and that work has gone on to become quite popular in various communities, not least of all the Java community. The community needed a slick integration for Spring users, and so we - the Spring team - reached out to the GraphQL Java project to see if we could collaborate on an integration. GraphQL Java is the basis of the Spring GraphQL project. It’s a lightning-fast integration for GraphQL that now powers the likes of Twitter.com. It’s fast and battle-tested. Spring GraphQL builds upon that solid foundation to provide a conversational component model for Spring developers that will feel natural to developers who have used Spring’s support for building MVC, REST, RSocket, and WebSocket-based controllers.', '01/12/2020', 'Spring GraphQL', 0, 2);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'This year, Oracle and the Java community released Java 17, the next long-term supported version of Java. It is awesome. It is also the latest long-term support release of Java, which means that if you’re conservative and wish to hew to the stable, well-supported versions of Java, this is the release for you. If you want the latest and greatest, this is also the right release for you (until Java 18 arrives). I love Java 17. And, now that GraalVM supports Java 17, there’s no reason for it not to be your main release, no matter what OpenJDK distribution you’re targeting. Java 8 is an inexcusably old and irrelevant version of Java. There’s no excuse to be using it today for any reason whatsoever except that perhaps you want to study the antiquities. Based on the version number, Java 17 is more than twice as good as Java 8. Here’s a list of my favorite features since Java 8.', '10/10/2020', 'Java 17', 0, 1);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'This platform-as-a-service offering jointly developed by the Spring team and Microsoft is growing like a weed, constantly being improved and hardened to do the work of delivering Spring Boot-applications to production as easily as can be. And 2021 was no different. This year saw the integration of managed virtual networks and autoscale, improved monitoring, easy deployments, Dynatrace integration, outbound public IPs, a Visual Studio Code integration, and full APM capabilities, and so much more. There are, of course, tons of references out there, too.', '05/03/2019', 'Azure Spring Cloud', 0, 2);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'Spring provides unmatched protocols to complement client-side developers who want to talk to services, supporting things like WebSockets, HTTP, REST, OAuth, RSocket, and GraphQL. But the opportunities for integration are getting better by the day. For example, I loved this recent post by the good Dr. Dave Syer introducing some of the possibilities for integation. In this blog, the good Dr. looks at things like Webjars, HTMX, Hotwired, and more.', '05/03/2019', 'The Client Side', 0, 2);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'Buildpacks get better and better every day! Buildpacks are a CNCF specification that provide a way to take applications and containerize them. Don’t want to write a Dockerfile? Me either! 90% of the time, they’re repetitive and add nothing to the world except wasteful redundancies. It’s not like nobody has figured out how to containerize a Java application before! So, buildpacks let you take an application artifact, be it a .jar, or a .war, or a .NET assembly or a Ruby on Rails application or a Node.js application or… whatever!… and turn it into a Docker image that you can then docker tag and docker push to the container registry of your choice. (May I recommend VMware Harbor?) Buildpacks do this using builders, of which there are a whole ecosystem’s worth to choose from. The Paketo project provides a ton of out-of-the-box builders. Buildpacks are fantastic in themselves, but I love that you can talk to the buildpacks API in different incarnations. Spring Boot provides out-of-the-box support for buldpacks through its Maven and Gradle plugins: mvn spring-boot:build-image, and you’re off to the containerized races! If you don’t want to set that all up for every build pipeline, consider using KPack, a Kubernetes operator that runs in-cluster and applies builders to artifacts whenever it perceives an update. You don’t have to leak the security credentials for your cluster into your CI pipeline, and you don’t have to re-build that container publication pipeline for each new module. Life is better all around! There’s also a buildpack to build Spring Native-powered GraalVM images, too! Simply go to the Spring Initializr (start.spring.io) and build a new project with Spring Native as a dependency. It’ll automatically retool the buildpacks support for native images.', '10/11/2019', 'Buildpacks', 0, 4);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'This article explores the different options that Spring Boot developers have for using Javascript and CSS on the client (browser) side of their application. Part of the plan is to explore some Javascript libraries that play well in the traditional server-side-rendered world of Spring web applications. Those libraries tend to have a light touch for the application developer, in the sense that they allow you to completely avoid Javascript, but still have nice a progressive "modern" UI. We also look at some more "pure" Javascript tools and frameworks. It’s kind of a spectrum, so as a TL;DR here is a list of the sample apps, in rough order of low to high Javascript content', '10/11/2019', 'Client Side Development with Spring Boot Applications', 0, 3);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'MongoDB’s flexible schema allows for multiple patterns when it comes to modeling relationships between entities. Also, for many use cases, a denormalized data model (storing related data right within a single document) might be the best choice, because all information is kept in one place, so that the application requires fewer queries to fetch all data. However, this approach also has its downsides, such as potential data duplication, larger documents, and the maximum document size. In general, MongoDB recommends using normalized data models when the advantages of embedding are neglected by the implications of duplication. In this blog post, we take a look at the different possibilities of linking documents with manual references and DBRefs when the need occurs to work with relations.DBRef is MongoDB’s native element to express references to other documents with an explicit format { $db : …, $ref : …, $id : … } that holds information about the target database, collection, and id value of the references element, best suited to link to documents distributed across different collections.Manual references, on the other hand, are simpler in structure (by storing only the id of the referenced document), but are, therefore, not as flexible when it comes to mixed collection references.', '02/01/2018', 'Spring Data MongoDB - Relation Modelling', 0, 4);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'Spring Cloud 2021.0.0 is finally out and with it you have Spring Cloud Function 3.2 While the full list of features, enhancements and bug fixes is available here, I’d like to call out few of them in this post and provide some details. gRPC Support In addition to an already existing support for invoking function via AWS Lambda, RSocket, Spring Cloud Stream etc., Spring Cloud Function now allows you to invoke function via gRPC. Two ways to benefit from it. Spring Message Given the wide adaption of Spring Messaging, one way of benefiting from gRPC support is by embracing Spring’s Message. Spring Cloud Function provides GrpcSpringMessage schema modeled after Spring’s Message. It is internally converted to Spring Message to benefit from all of the existing support for Spring Messaging.', '02/01/2018', 'Spring Cloud Function 3.2 is out!', 0, 3);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'ACCEPTED', 'Speaking of CloudEvents… In version 3.1 we’ve introduces support for CloudEvents and you can read part-1 and part-2 of the blog posts on the subject.This release contains some additional enhancements and bug fixes as well as support for io.cloudevents.CloudEvent type via integration with CloudEvents Java SDK. And to combine gRPC and CloudEvents we also provide a dedicated example demonstrating CloudEvents interaction over gRPC.', '01/01/2018', 'Enhanced CloudEvents Support', 0, 4);