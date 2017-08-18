Data structure experiments
==========================

You might find horrible and bloody experiments on different data structures in the repository. And all this is done just to gain better understanding of its behaviour on modern hardware.

### Run Java benchmarks

```sh
$ gradle jmhJar
$ java -jar build\libs\sequential-1.0-jmh.jar -f 1 -i 5 -wi 5 -e .*ArrayQueue.* -gc true -tu us -prof hs_comp -prof hs_gc -prof hs_rt
```
