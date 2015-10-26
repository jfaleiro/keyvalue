# keyvalue

In-memory key value store-with-expiration

## Installation

```
git clone https://github.com/jfaleiro/keyvalue.git
cd keyvalue
gradle check
```

## Use

```java
final KeyValue<String, Integer> kv = new DefaultKeyValue<>();
kv.put("A", 1);
kv.put("B", 2, 200); // will expire after 200ms
final int i = kv.get("A");
kv.delete("B");
```

More examples under unit tests.

## Benchmarking

```
gradle test jmh
```

Should yield something like

```
# Warmup Iteration   4: n = 14942, mean = 0 us/op, p{0.00, 0.50, 0.90, 0.95, 0.99, 0.999, 0.9999, 1.00} = 0, 0, 0, 0, 1, 3, 122, 169 us/op
# Warmup Iteration   5: n = 15864, mean = 0 us/op, p{0.00, 0.50, 0.90, 0.95, 0.99, 0.999, 0.9999, 1.00} = 0, 0, 0, 0, 0, 2, 39, 66 us/op
Iteration   1: n = 19005, mean = 0 us/op, p{0.00, 0.50, 0.90, 0.95, 0.99, 0.999, 0.9999, 1.00} = 0, 0, 0, 0, 0, 1, 23, 38 us/op
Iteration   2: n = 18918, mean = 0 us/op, p{0.00, 0.50, 0.90, 0.95, 0.99, 0.999, 0.9999, 1.00} = 0, 0, 0, 0, 0, 1, 15, 26 us/op
(...)
```





