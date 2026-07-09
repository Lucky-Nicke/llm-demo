```html
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script>
function login() {
    var xhr = new XMLHttpRequest();
    var url = "http://106.14.170.156:8083/server?account=test&password=111111";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("method", "login");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var result = JSON.parse(xhr.responseText);
            document.getElementById("myDiv").innerHTML = "<h2>登录结果</h2><p>" + result.msg + "</p>";
        }
    };
    xhr.send();
}
</script>
</head>
<body>

<div id="myDiv"><h2>登录结果</h2></div>
<button type="button" onclick="login()">登录</button>

</body>
</html>
```

---

```java
public class demo2 {
    private static Thread printThread;

    public static void main(String[] args) {
        int maxNumber = Integer.parseInt(args[0]);
        int stopTime = Integer.parseInt(args[1]);
        startPrint(maxNumber);
        stopPrint(stopTime);
    }

    static void startPrint(int maxNumber) {
        printThread = new Thread(() -> {
            for (int i = 1; i <= maxNumber; i++) {
                if (Thread.currentThread().isInterrupted()) break;
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        printThread.setDaemon(true);
        printThread.start();
    }

    static void stopPrint(int stopTime) {
        Thread stopThread = new Thread(() -> {
            try {
                Thread.sleep((long) stopTime * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (printThread != null) {
                printThread.interrupt();
            }
        });
        stopThread.setDaemon(true);
        stopThread.start();
    }
}
```

---

```java
public class demo1 {
    public static void main(String[] args) {
        User user = new User(45494);
        if (user.hasPrivilege(user.USER_PRIVILEGE_DELETE)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    static class User {
        public int USER_PRIVILEGE_DELETE = 512;
        private int privilege;

        public User(int privilege) {
            this.privilege = privilege;
        }

        public boolean hasPrivilege(int privilegeFlag) {
            return (privilege & privilegeFlag) != 0;
        }
    }
}
```

---

```java
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class demo3 {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://www.corget.com/DownloadServer/download/Files/order");
        byte[] data;
        try (InputStream in = url.openStream();
             ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            byte[] tmp = new byte[1024];
            int n;
            while ((n = in.read(tmp)) != -1) buf.write(tmp, 0, n);
            data = buf.toByteArray();
        }

        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        byte cmd = bb.get();
        long id = bb.getLong();
        short dist = bb.getShort();
        short fee = bb.getShort();

        // 读取空终止字符串
        String createTime = readCString(bb);
        String s2 = readCString(bb);
        String s3 = readCString(bb);
        String urlStr = readCString(bb);

        System.out.println("id:" + id);
        System.out.println("url:" + urlStr);
    }

    static String readCString(ByteBuffer bb) throws Exception {
        StringBuilder sb = new StringBuilder();
        while (bb.hasRemaining()) {
            byte b = bb.get();
            if (b == 0) break;
            sb.append((char) b);
        }
        return sb.toString();
    }
}
```

---

```java
import java.util.*;

public class demo4 {
    static class Message {
        String userId;
        String message;
        long createTime;
        String type;

        Message(String userId, String message, long createTime, String type) {
            this.userId = userId;
            this.message = message;
            this.createTime = createTime;
            this.type = type;
        }

        @Override
        public String toString() {
            return userId + "," + message + "," + createTime + "," + type;
        }
    }

    static List<Message> parse(List<String> lines) {
        List<Message> result = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", 4);
            result.add(new Message(parts[0], parts[1], Long.parseLong(parts[2]), parts[3]));
        }
        return result;
    }

    static List<Message> filterError(List<Message> list) {
        List<Message> result = new ArrayList<>();
        for (Message m : list) {
            if ("ERROR".equals(m.type)) result.add(m);
        }
        return result;
    }

    static Map<String, Integer> countByUser(List<Message> list) {
        Map<String, Integer> map = new HashMap<>();
        for (Message m : list) {
            map.put(m.userId, map.getOrDefault(m.userId, 0) + 1);
        }
        return map;
    }

    static List<Message> queryByTime(List<Message> list, long start, long end) {
        List<Message> result = new ArrayList<>();
        for (Message m : list) {
            if (m.createTime >= start && m.createTime < end) result.add(m);
        }
        return result;
    }

    static List<Message> sortByTime(List<Message> list) {
        List<Message> result = new ArrayList<>(list);
        result.sort((a, b) -> {
            if (a.createTime != b.createTime) return Long.compare(a.createTime, b.createTime);
            return a.userId.compareTo(b.userId);
        });
        return result;
    }

    public static void main(String[] args) {
        List<String> lines = Arrays.asList(
            "1001,hello world,1700000000000,INFO",
            "1002,error occurred,1700000005000,ERROR",
            "1001,second msg,1700000010000,INFO",
            "1003,test data,1700000020000,INFO"
        );

        List<Message> messages = parse(lines);
        System.out.println("--- parse ---");
        for (Message m : messages) System.out.println(m);

        System.out.println("--- filterError ---");
        for (Message m : filterError(messages)) System.out.println(m);

        System.out.println("--- countByUser ---");
        System.out.println(countByUser(messages));

        System.out.println("--- queryByTime [1700000000000, 1700000010000) ---");
        for (Message m : queryByTime(messages, 1700000000000L, 1700000010000L)) System.out.println(m);

        System.out.println("--- sortByTime ---");
        for (Message m : sortByTime(messages)) System.out.println(m);
    }
}
```
