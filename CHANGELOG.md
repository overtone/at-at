# at-at Changelog

## 1.1.0
_14th Jan 2013_

* Added new fn `interspaced` which will call fun repeatedly with a
  specified interspacing.
* Added missing trailing quotes when printing schedule.

## 1.2.0
_28th May 2013_

* BREAKING CHANGE - Remove support for specifying stop-delayed? and
  stop-periodic? scheduler strategies.
* Jobs now correctly report as no longer being scheduled when pool is shutdown.
