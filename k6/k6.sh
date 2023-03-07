echo '#############  Sync controller  #############' > k6/k6_results.txt
k6 run k6/k6_sync.js >> k6/k6_results.txt
echo '#############  All async controller  #############' >> k6/k6_results.txt
k6 run k6/k6_all_async.js >> k6/k6_results.txt
echo '#############  Async just controller  #############' >> k6/k6_results.txt
k6 run k6/k6_async_just_controller.js >> k6/k6_results.txt
