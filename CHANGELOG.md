# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
----------------------

### Added

- _Nothing yet_

### Changed

- _Nothing yet_

### Fixed

- _Nothing yet_

### CI/CD

- _Nothing yet_

## [5.1.1] - 2025-09-25
----------------------

### Changed

- Keep the README JitPack badges in sync with a managed block

### Build

- Require `-PenableSigning=true` and signing credentials before running PGP signing to avoid missing `.asc` artifacts in unsigned environments

### CI/CD

- Run GitHub Actions Gradle checks on Temurin JDK 17 with Gradle caching

## [5.1.0] - 2025-09-25
----------------------

### Added

- Add multi-language support
- Add Arabic translations
- Expand README with project overview, install guides, and usage notes

### Changed

- Align Gradle and publishing configuration with modern JitPack requirements
- Refresh sample app dependencies to rely on shared JitPack stack

### Fixed

- Resolve duplicate `Photo` class collisions by reusing the upstream model
- Exclude duplicate license/notice resources from the sample packaging task
- Restore theme and photo placeholders in immunization screens

### Tests

- Replace PowerMock usage with Mockito alternatives to simplify unit tests

### CI/CD

- Configure JitPack builds to run on JDK 17
- Automate README badge maintenance and warm up the master snapshot build
