package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.lang.Thread.sleep;


public class ConcurrencyAsyncExercises {
    private static Logger log = Logger.getLogger(ConcurrencyAsyncExercises.class.getName());
    // COMPLETABLE FUTURE
    // CompletableFuture.supplyAsync()
    /*
    - Write a method that returns a CompletableFuture that supplies a random integer after a delay. Use supplyAsync.
    - Create a list of URLs and use supplyAsync to fetch their content concurrently. Collect the results into a list.
     */
   static Executor executor = Executors.newFixedThreadPool(2);
   public static CompletableFuture<Integer> supplyInteger = CompletableFuture.supplyAsync(() -> {
        log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ThreadLocalRandom.current().nextInt(1, 101);
    },executor);
   public static List<String> findTitles(List<String> urls) {
      List<CompletableFuture<String>> titleFutures = urls.stream()
          .map(url ->CompletableFuture.supplyAsync(() -> {
              log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
              JSONObject jsonObject;
              try (HttpClient client = HttpClient.newHttpClient()) {
                  HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
                  HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                  jsonObject = new JSONObject(response.body());
              } catch (URISyntaxException | IOException | InterruptedException e) {
                  throw new RuntimeException(e);
              }
              return jsonObject.getString("title");
          }) )
          .toList();
      return titleFutures.stream().map(CompletableFuture::join).toList();
  }

  //  CompletableFuture.thenApply()
  /*
  - Write a method that returns a CompletableFuture which fetches a string, then applies a transformation to convert it to uppercase.
  - Given a list of integers, use CompletableFuture to double each integer asynchronously.
  */
  public static String stringUpper(String url) {
      CompletableFuture<String> getString = CompletableFuture.supplyAsync(() ->  {
          log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
          JSONObject jsonObject;
          try (HttpClient client = HttpClient.newHttpClient()) {
              HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
              HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
              jsonObject = new JSONObject(response.body());
          } catch (URISyntaxException | IOException | InterruptedException e) {
              throw new RuntimeException(e);
          }
          return jsonObject.getString("title");
      }).thenApply(String::toUpperCase);
      return getString.join();
  }
  public static List<Integer> doubleNumbers(List<Integer> list){
      List<CompletableFuture<Integer>> doubleList = list.stream()
              .map(n -> CompletableFuture.supplyAsync(() -> 2 * n)).toList();
       return doubleList.stream().map(CompletableFuture::join).toList();
  }

  // CompletableFuture.thenCompose()
  /*
  - Write a method that returns a CompletableFuture which fetches a user ID, then fetches user details based on that ID asynchronously.
  - Implement a sequence of asynchronous tasks where one task fetches data, and the next task processes the fetched data.
  */
  public static JSONObject randomUserDetails(){
     String uri = "https://myfakeapi.com/api/users/";
     CompletableFuture<JSONObject> getUser = CompletableFuture.supplyAsync(() -> {
                 log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
                 return ThreadLocalRandom.current().nextInt(1, 1000);
             })
             .thenComposeAsync(n -> CompletableFuture.supplyAsync(() -> {
                 log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
                 JSONObject jsonObject;
                 try (HttpClient client = HttpClient.newHttpClient()) {
                     HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri + String.valueOf(n))).GET().build();
                     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                     jsonObject = new JSONObject(response.body());
                 } catch (URISyntaxException | IOException | InterruptedException e) {
                     throw new RuntimeException(e);
                 }
                 return jsonObject;
             }));
     return getUser.join();
  }
      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      public static class Automovil{
           @JsonProperty("car_model")
           private String carModel;
           @JsonProperty("car_model_year")
           private int carModelYear;
           @JsonProperty("car_vin")
           private String carVin;
           @JsonProperty("car")
           private String car;
           @JsonProperty("price")
           private String price;
           @JsonProperty("car_color")
           private String carColor;
           @JsonProperty("id")
           private int id;
           @JsonProperty("availability")
           private boolean availability;
      }
      public static Long getNumberOfCars(String color){
          String uri = "https://myfakeapi.com/api/cars/";
          CompletableFuture<Long> getNumber = CompletableFuture.supplyAsync(() -> {
              log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
              ObjectMapper objectMapper = new ObjectMapper();
              List<Automovil> carList = new ArrayList<>();
              try (HttpClient client = HttpClient.newHttpClient()) {
                  HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
                  HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                  JsonNode rootNode = objectMapper.readTree(response.body());
                  JsonNode carsNode = rootNode.get("cars");
                  List<Automovil> cars = objectMapper.readValue(carsNode.toString(), new TypeReference<List<Automovil>>() {});
                  carList.addAll(cars);
              } catch (URISyntaxException | IOException | InterruptedException e) {
                  throw new RuntimeException(e);
              }
              return carList.stream().filter(auto -> auto.carColor.equalsIgnoreCase(color)).count();
          });
          return getNumber.join();
      }

      //CompletableFuture.thenCombine()
      /*
      - Write a method that combines the results of two independent CompletableFuture tasks and returns their combined result.
      - Create two CompletableFuture tasks, one fetching weather data and another fetching news headlines, then combine their results.

      */
      public static CompletableFuture<String> combineTexts = CompletableFuture.supplyAsync(() -> "Nice to")
              .thenCombine(CompletableFuture.supplyAsync(() -> " meet you"), (k1, k2) -> k1 + k2);

      /* START AUXILIARY CODE */
      private static Map<String,String> useTraduct(){
           Map<String,String> traduc = Collections.synchronizedMap(new HashMap<String,String>());
           traduc.put("Forbes","Hoboken,NJ") ;
           traduc.put("The Telegraph","London,UK") ;
           traduc.put("Fox News","Manhatan,NY") ;
           traduc.put("BBC.com","London,UK") ;
           traduc.put("The New York Times","Manhatan,NY") ;
           return traduc;
      }
      public static Map<String, String> reverseMap(Map<String, String> map) {
            Map<String, String> reverseMap = new HashMap<>();
            for(Map.Entry<String,String> entry: map.entrySet()){
                reverseMap.computeIfAbsent(entry.getValue(), k -> entry.getKey());
            }
            return reverseMap;
      }
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       static class Wind{
           private int chill;
           private String direction;
           private int speed;
       }
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       static class Atmosphere{
           private float humidity;
           private float visibility;
           private float pressure;
       }
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
       static class Astronomy{
           private String sunrise;
           private String sunset;
        }
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       static class Condition {
           private int temperature;
           private String text;
           @JsonIgnore
           private int code;
      }
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       static class Weather {
           private int pubDate;
           private Wind wind;
           private Atmosphere atmosphere;
           private Astronomy astronomy;
           private Condition condition;
       }
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       public static class News {
            private String title;
            private String snippet;
            private String publisher;
            private String timestamp;
            private String newsUrl;
            @JsonIgnore
            private JSONObject images;
            @JsonIgnore
            private boolean hasSubnews;
            @JsonIgnore
            private List<News> subnews;
       }
      static String rapidApi = "...";
      private static Map<String,List<String>> getGoogleNews(List<String> newsOutlets) throws IOException, InterruptedException {
          Map<String,List<String>> newsHeadlines = new HashMap<String,List<String>>();
          HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create("https://google-news13.p.rapidapi.com/technology?lr=en-US"))
                  .header("x-rapidapi-key", rapidApi)
                  .header("x-rapidapi-host", "google-news13.p.rapidapi.com")
                  .method("GET", HttpRequest.BodyPublishers.noBody())
                  .build();
          HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
          if (response.statusCode() == 200) {
              ObjectMapper objectMapper = new ObjectMapper();
              JsonNode rootNode = objectMapper.readTree(response.body());
              JsonNode newsNode = rootNode.get("items");
              List<News> news = objectMapper.readValue(newsNode.toString(), new TypeReference<List<News>>(){});
              news.stream().forEach(n -> {
                  if(newsOutlets.contains(n.getPublisher())){
                      if(!newsHeadlines.containsKey(n.getPublisher()) ){
                         newsHeadlines.put(n.getPublisher(), new ArrayList<>());
                      }
                      List<String> temp = newsHeadlines.get(n.getPublisher());
                      temp.add(n.getTitle());
                      newsHeadlines.put(n.getPublisher(), temp);
                  }
              });
          }
          return newsHeadlines;
      }
      private static Map<String,Weather> getWeather(String outlet, String location) throws IOException, InterruptedException {
          String uri = STR."https://yahoo-weather5.p.rapidapi.com/weather?location=\{location}&format=json&u=f";
          Map<String,Weather> weather = new HashMap<>();
          HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create(uri))
                  .header("X-RapidAPI-Key", rapidApi2)
                  .header("X-RapidAPI-Host", "yahoo-weather5.p.rapidapi.com")
                  .method("GET", HttpRequest.BodyPublishers.noBody())
                  .build();
          HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
          if(response.statusCode() == 200){
             ObjectMapper objectMapper = new ObjectMapper();
             JsonNode rootNode = objectMapper.readTree(response.body());
             JsonNode weatherNode = rootNode.get("current_observation");
             Weather w = objectMapper.readValue(weatherNode.toString(), new TypeReference<Weather>(){});
             weather.put(outlet, w);
          }
          return weather;
      }
      static CompletableFuture<ConcurrentHashMap<String,Weather>> weatherInfo(Set<String> locations) {
          ConcurrentHashMap<String,Weather> map = new ConcurrentHashMap<>();
          List<CompletableFuture<Weather>> weatherFutures = locations.stream()
            .map(location -> CompletableFuture.supplyAsync(() -> {
                log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}: getWeatherFutures - \{location}");
                Weather w = new Weather();
                StringBuilder str = new StringBuilder();
                str.append("https://yahoo-weather5.p.rapidapi.com/weather?location=").append(location).append("&format=json");
                HttpResponse<String> response = null;
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(str.toString()))
                        .header("X-RapidAPI-Key", rapidApi1)
                        .header("X-RapidAPI-Host", "yahoo-weather5.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                try(HttpClient client = HttpClient.newHttpClient()) {
                    try {
                        response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if(response.statusCode() == 200){
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            JsonNode rootNode = objectMapper.readTree(response.body());
                            JsonNode weatherNode = rootNode.get("current_observation");
                            w = objectMapper.readValue(weatherNode.toString(), new TypeReference<Weather>() {});
                            map.put(location,w);
                        } catch (JsonProcessingException e) {
                            log.severe(STR."something went wrong: \{e.getMessage()}");
                        }
                    }
                }
                return w;
            })).toList();
          return CompletableFuture.supplyAsync(() -> {return map;});
      }
      static CompletableFuture<Map<Integer,String>> getNews() {
          CompletableFuture<Map<Integer,String>> newsFutures = CompletableFuture.supplyAsync(() -> {
              HttpResponse<String> response = null;
              HttpRequest request = HttpRequest.newBuilder()
                      .uri(URI.create("https://google-news13.p.rapidapi.com/technology?lr=en-US"))
                      .header("x-rapidapi-key", rapidApi1)
                      .header("x-rapidapi-host", "google-news13.p.rapidapi.com")
                      .method("GET", HttpRequest.BodyPublishers.noBody())
                      .build();
              try (HttpClient client = HttpClient.newHttpClient()) {
                  response = client.send(request, HttpResponse.BodyHandlers.ofString());
              } catch (IOException | InterruptedException e) {
                  throw new RuntimeException(e);
              }
              Map<Integer,String> result = new HashMap<>();
              result.put(response.statusCode(), response.body());
              return result;
          });
          return newsFutures;
      }
      /* END AUXILIARY CODE */

      public static Map<String, Map<Weather, List<String>>> getWeatherAndNews() throws ExecutionException, InterruptedException {
          List<String> newsOutlets = new ArrayList<>(useTraduct().keySet());
          Set<String> locations = new HashSet<>(useTraduct().values());
          Map<String, Map<Weather, List<String>>> weatherAndNews = new HashMap<>();
          Map<String, List<String>> newsHeadlines = new TreeMap<>();
          Map<String, Weather> weatherLocationMap = new TreeMap<>();
          Map<Map<String,Weather>, Map<String,List<String>>>result = new LinkedHashMap<>();
          CompletableFuture<Map<Map<String,Weather>, Map<String,List<String>>>> combinedFutures = getNews()
              .thenCombine(weatherInfo(locations), (news,weather) -> {
                  if(news.containsKey(200)){
                      ObjectMapper objectMapper = new ObjectMapper();
                      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                      try {
                          JsonNode rootNode = objectMapper.readTree(news.get(200).toString());
                          JsonNode newsNode = rootNode.get("items");
                          List<News> newsList = objectMapper.readValue(newsNode.toString(), new TypeReference<List<News>>(){});
                          newsList.stream().forEach(n -> {
                              if(newsOutlets.contains(n.getPublisher())){
                                  if(!newsHeadlines.containsKey(n.getPublisher()) ){
                                      newsHeadlines.put(n.getPublisher(), new ArrayList<>());
                                  }
                                  List<String> temp = newsHeadlines.get(n.getPublisher());
                                  temp.add(n.getTitle());
                                  newsHeadlines.put(n.getPublisher(), temp);
                              }
                          });
                      } catch (JsonProcessingException e) {
                          log.info("Something went wrong: " + e.getMessage());
                      }
                  }
                  newsOutlets.forEach(o -> {
                      String loc = useTraduct().get(o);
                      weatherLocationMap.put(o,weather.get(loc));
                  });
                  log.info(STR."weatherLocationMap before return \n\{weatherLocationMap.toString()}");
                  result.put(weatherLocationMap,newsHeadlines);
                  return result;
              });
          Map<Map<String,Weather>, Map<String,List<String>>> combinedMap = combinedFutures.get();
          while(!combinedFutures.isDone()) {}
          Set<Map<String, Weather>> set1 = new LinkedHashSet<>(combinedMap.keySet());
          Set<Map<String, List<String>>> set2 = new LinkedHashSet<>(combinedMap.values());
          newsOutlets.stream().sorted().forEach(out -> {
              //Map<Weather,List<String>> temp = new TreeMap<>();
              Iterator<Map<String, Weather>> it1 = set1.iterator();
              Iterator<Map<String, List<String>>> it2 = set2.iterator();
              while(it1.hasNext())  log.info(it1.next().toString());
              while(it2.hasNext())  log.info(it2.next().toString());
              //weatherAndNews.put(out,)
          });
          return null;
      }

    // CompletableFuture.allOf()
    /*
    - Given a list of CompletableFuture tasks, use allOf to wait for all tasks to complete, then collect their results.
    - Create multiple CompletableFuture tasks that simulate file downloads, and use allOf to perform an action once all
      downloads are complete.
    */
    public static List<String> practice1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
            try {
                sleep(ThreadLocalRandom.current().nextInt(1000, 5000));}
            catch (InterruptedException e) {log.severe(e.getMessage());}
            log.info(STR."Task1 Completed");
            return "Task1 completed";
        });
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
            try {
                sleep(ThreadLocalRandom.current().nextInt(1000, 5000));}
            catch (InterruptedException e) {log.severe(e.getMessage());}
            log.info(STR."Task2 Completed");
            return "Task2 completed";
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
            try {
                sleep(ThreadLocalRandom.current().nextInt(1000, 5000));}
            catch (InterruptedException e) {log.severe(e.getMessage());}
            log.info(STR."Task3 Completed");
            return "Task3 completed";
        });
        List<CompletableFuture<String>> completableFutures = Arrays.asList(task1,task2,task3);
        CompletableFuture<Void> resultantCf = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
        CompletableFuture<List<String>> allFutureResults = resultantCf.thenApply(t -> completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        return allFutureResults.get();
    }

    // CompletableFuture.anyOf()
    /*
    - Write a method that uses anyOf to return the result of the first completed CompletableFuture from a list of tasks.
    - Create multiple CompletableFuture tasks with varying delays, and use anyOf to print the result of the fastest task.
    */
    public static String practice2() throws ExecutionException, InterruptedException {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
            try {
                sleep(ThreadLocalRandom.current().nextInt(1000, 5000));}
            catch (InterruptedException e) {log.severe(e.getMessage());}
            log.info(STR."Task1 Completed");
            return "Task1 completed";
        });
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
            try {
                sleep(ThreadLocalRandom.current().nextInt(1000, 5000));}
            catch (InterruptedException e) {log.severe(e.getMessage());}
            log.info(STR."Task2 Completed");
            return "Task2 completed";
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()}");
            try {
                sleep(ThreadLocalRandom.current().nextInt(1000, 5000));}
            catch (InterruptedException e) {log.severe(e.getMessage());}
            log.info(STR."Task3 Completed");
            return "Task3 completed";
        });
        List<CompletableFuture<String>> completableFutures = Arrays.asList(task1,task2,task3);
        CompletableFuture<Object> resultantCf = CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
        return "Final Result - " + resultantCf.get();
    }


    // EXECUTOR SERVICE
    // ScheduledExecutorService.schedule()
    /*
    - Use ScheduledExecutorService to schedule a task that prints "Hello, World!" after a 5-second delay.
    */
    static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    static class Task implements Callable<String> {
        private final String name;
        public Task(String name) {this.name = name;}
        @Override
        public String call() throws Exception {
            return STR."Task \{name} executed on : \{LocalDateTime.now().toString()}";
        }
    }
    public static void executeTaskWithDelayOnce() throws Exception {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        System.out.println(STR."Task scheduled to execute after 5 seconds at : \{LocalDateTime.now().toString()}");
        Task task = new Task("Hello World!");
        ScheduledFuture<?> result = executor.schedule(task, 5, TimeUnit.SECONDS);
        System.out.println(STR."Shutdown and await requested at : \{LocalDateTime.now().toString()}");
        shutdownAndAwaitTermination(executor);
        System.out.println(STR."\{task.call()}");
    }

    // ScheduledExecutorService.scheduleAtFixedRate()
    /*
    - Write a method that uses scheduleAtFixedRate to print the current time every second.
    - Use scheduleAtFixedRate to periodically fetch and print stock prices every 10 seconds.
    */
    public static void printCurrentTime() throws Exception {
        AtomicInteger count = new AtomicInteger();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable currentTime = () -> {
            System.out.println(STR."Current time is: \{LocalTime.now()}");
            count.getAndIncrement();
        };
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(currentTime, 2, 2, TimeUnit.SECONDS);
        while (true) {
            System.out.println("count :" + count);
            sleep(1000);
            if (count.get() == 5) {
                System.out.println("Count is 5, cancel the scheduledFuture!");
                scheduledFuture.cancel(true);
                executor.shutdown();
                break;
            }
        }
    }
    public static void fetchStockPrices() throws Exception {
        AtomicInteger count = new AtomicInteger();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable X1bchPrice = () -> {
            try {
                String url = "https://coingecko.p.rapidapi.com/exchanges/1bch";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("x-rapidapi-key", rapidApi3)
                        .header("x-rapidapi-host", "coingecko.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (Exception e) {
                log.info(STR."Something went wrong: \{e.getMessage()}");
            } finally {
                count.getAndIncrement();
            }
        };
        ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(X1bchPrice, 1, 10, TimeUnit.SECONDS);
        while (true) {
            System.out.println("count :" + count);
            sleep(1000);
            if (count.get() == 5) {
                System.out.println("Count is 5, cancel the scheduledFuture!");
                scheduledFuture.cancel(true);
                executor.shutdown();
                break;
            }
        }
    }

    // ScheduledExecutorService.scheduleWithFixedDelay()
    /*
    - Schedule a task to fetch data from an API at a fixed delay.
    - Schedule a periodic task to check system resource usage every 15 seconds, ensuring there's a fixed delay between
      each check.
    */
    public static void executeMealSearchRepeated() throws Exception {
        AtomicInteger count = new AtomicInteger();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable randomMeal = () -> {
            try {
                String url = "https://www.themealdb.com/api/json/v1/1/random.php";
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                        .method("GET", HttpRequest.BodyPublishers.noBody()).build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (Exception e) {
                log.info(STR."Something went wrong: \{e.getMessage()}");
            } finally {
                count.getAndIncrement();
            }
        };
        ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(randomMeal, 2, 2, TimeUnit.SECONDS);
        while (true) {
            System.out.println("count :" + count);
            sleep(1000);
            if (count.get() == 5) {
                System.out.println("Count is 5, cancel the scheduledFuture!");
                scheduledFuture.cancel(true);
                executor.shutdown();
                break;
            }
        }
    }
    public static void systemStatistics() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable stats = () -> {
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            long startTime = runtimeBean.getStartTime();
            long uptime = runtimeBean.getUptime();
            double cpuUsage = (double) uptime / (System.currentTimeMillis() - startTime);
            System.out.println(STR."\nEstimated System CPU Usage: \{cpuUsage}");
            System.out.println(STR."System Load Average: \{osBean.getSystemLoadAverage()}");
            count.getAndIncrement();
        };
        ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(stats, 0, 5, TimeUnit.SECONDS);
        while (true) {
            System.out.print(".");
            sleep(1000);
            if (count.get() == 5) {
                System.out.println("Count is 5, cancel the scheduledFuture!");
                scheduledFuture.cancel(true);
                executor.shutdown();
                break;
            }
        }
    }

    // CUSTOM THREAD POOLS
    // Creating Custom Thread Pool
    /*
    - Create a custom thread pool with a fixed number of threads and submit multiple tasks to it. Print the result of
      each task.
    - Implement a thread pool executor with a bounded queue and test it with tasks that simulate CPU-bound operations.
    */
    public static void fixedNumberOfThreads() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Map<String,String>> future1 = executorService.submit(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()} - \"Hello World\".toLowerCase()");
            Map<String, String> map = new HashMap<>();
            map.put(Thread.currentThread().getName(), "Hello World".toLowerCase());
            TimeUnit.SECONDS.sleep(3);
            return map;
        });
        Future<Map<String,String>> future2 = executorService.submit(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()} - \"This is a test\".toUpperCase()");
            Map<String, String> map = new HashMap<>();
            map.put(Thread.currentThread().getName(), "This is a test".toUpperCase());
            TimeUnit.SECONDS.sleep(1);
            return map;
        });
        Future<Map<String,String>> future3 = executorService.submit(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()} - \"next string\".toLowerCase()");
            Map<String, String> map = new HashMap<>();
            map.put(Thread.currentThread().getName(), "next string");
            TimeUnit.SECONDS.sleep(2);
            return map;
        });
        Future<Map<String,String>> future4 = executorService.submit(() -> {
            log.info(STR."THREAD WORKER \{Thread.currentThread().getName()} - \"Last String\".toUpperCase()");
            Map<String, String> map = new HashMap<>();
            map.put(Thread.currentThread().getName(), "Last String");
            TimeUnit.SECONDS.sleep(1);
            return map;
        });
        try {
            System.out.println(future1.get().toString());
            System.out.println(future2.get().toString());
            System.out.println(future3.get().toString());
            System.out.println(future4.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            log.severe(e.getMessage());
        }
    }
    public static void boundedQueue(String str) throws InterruptedException {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> System.out.println(str));
    }

    // Configuring Thread Pool       -->  In different files
    /*
    - Configure a thread pool with a core pool size, maximum pool size, and keep-alive time.
      Submit tasks and observe the behavior.
    - Write a program that demonstrates the behavior of a cached thread pool by submitting short-lived tasks.
    */



    public static void main(String[] args) throws Exception {
        /*
        System.out.println(STR."The number is \{supplyInteger.get()}");
        List<String> urls = new ArrayList<>();
        urls.add("https://jsonplaceholder.typicode.com/posts/1");
        urls.add("https://jsonplaceholder.typicode.com/posts/2");
        urls.add("https://jsonplaceholder.typicode.com/posts/3");
        System.out.println(findTitles(urls));
        System.out.println(stringUpper("https://jsonplaceholder.typicode.com/posts/4"));
        System.out.println(doubleNumbers(Arrays.asList(1,3,5,2,7,8)));
        System.out.println(randomUserDetails());
        String color1 = "red";
        String color2 = "maroon";
        System.out.println(STR."Number of \{color1} cars: \{getNumberOfCars(color1)}");
        System.out.println(STR."Number of \{color2} cars: \{getNumberOfCars(color2)}");
        System.out.println(combineTexts.get());
        */
        /*
        Map<String,String> map = useTraduct();
        System.out.println(new ArrayList<>(map.keySet()));
        System.out.println(getGoogleNews(new ArrayList<>(map.keySet())));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                Map<String,Weather> res = getWeather(entry.getKey(), entry.getValue());
                for(String key : res.keySet()){
                    System.out.println(STR."\{key}: \{res.get(key).getCondition().getTemperature()}");
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        */
        /* TIENE QUE ESPERAR A FINES DE JUNIO */
        /*Map<String, Map<Weather, List<String>>> map = getWeatherAndNews();
       for(Map.Entry<String,Map<Weather, List<String>>> entry1 : map.entrySet()){
            log.info(entry1.getKey().toUpperCase());
            for(Map.Entry<Weather,List<String>> entry2 : entry1.getValue().entrySet()){
                log.info(STR."WEATHER DATA: \n\{entry2.getKey()}");
                log.info(STR."NEWS: \n\{entry2.getValue()}");
            }
        }*/
        /*
        log.info(practice1().toString());
        log.info(practice2());
        executeTaskWithDelayOnce();
        executeMealSearchRepeated();
        printCurrentTime();
        fetchStockPrices();
        systemStatistics();
        */
        //boundedQueue("Hello World!");
        //fixedNumberOfThreads();


        System.exit(0);
    }

}









