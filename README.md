
# Async Task Manager

Accepts HTTP requests to start long-running (blocking) tasks

✅ Tracks tasks by ID

✅ Allows manual cancellation via HTTP

✅ Supports timeout

✅ Runs tasks asynchronously using a shared thread pool




## API Reference

#### Submit the new task

```http
  GET /api/v1/tasks/start?durationMs=5000&timeoutMs=10000
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `durationMs` | `string` | **Required**. durationMs |
| `timeoutMs`  | `string` | **Required**. timeoutMs |

Curl Request
```http
  curl --location --request POST 'http://localhost:8080/api/v1/tasks/cancel/b5f6e5ac-b53a-4dd9-8161-c4a14e1230c2'
```

#### Get all submitted task

```http
  GET /api/v1/tasks/all
```
Curl Request
```http
  curl --location 'http://localhost:8080/api/v1/tasks/all'
```


#### cancel the submitted task

```http
  curl --location --request POST 'http://localhost:8080/api/v1/tasks/cancel/b5f6e5ac-b53a-4dd9-8161-c4a14e1230c2'
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `taskId` | `string` | **Required**. taskId |

Curl Request
```http
  curl --location --request POST 'http://localhost:8080/api/v1/tasks/cancel/b5f6e5ac-b53a-4dd9-8161-c4a14e1230c2'
```



