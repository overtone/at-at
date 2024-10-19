# 1.4.65 (2024-10-19 / d813dc5)

- Fix reflection warnings

# 1.3.58 (2023-11-26 / cc6975b)

- Add exception handling through `uncaught-exception-handler`
- Make our thread pool threads recognizable by adding `at-at` to the thread name
- Add pprint handlers for records
- Add type hints to avoid reflection, and to be Babashka/GraalVM compatible
- Make `shutdown-pool!` public

## 1.2.0
_28th May 2013_

* BREAKING CHANGE - Remove support for specifying stop-delayed? and
  stop-periodic? scheduler strategies.
* Jobs now correctly report as no longer being scheduled when pool is shutdown.

## 1.1.0
_14th Jan 2013_

* Added new fn `interspaced` which will call fun repeatedly with a
  specified interspacing.
* Added missing trailing quotes when printing schedule.