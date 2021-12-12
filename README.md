# AtmanaTodoApp
[![check](https://github.com/siddhantkumarupmanyu/AtmanaTodoApp/actions/workflows/check.yml/badge.svg)](https://github.com/siddhantkumarupmanyu/AtmanaTodoApp/actions/workflows/check.yml) <br /> <br />
Assignment Project. <br />
It has two screens, one for Remote Data, which we cannot add or edit. And another screen which contains local data. <br />
Room is used for storing data locally. <br />
Since, I am in love with TDD, you can know more by looking at the code. It can answer better than me :)

https://user-images.githubusercontent.com/66074842/145705769-4f6edf65-3fb4-4192-bd3a-0730ff132ac1.mp4

## Features
- Pagination. i.e. automatically load data at reaching end
- Progress Bar when loading
- Automatically Refresh when local data is Added or Edited.
- Uses Coroutines, following Structured Concurrency Principles
- Uses Flow, instead of relying on LiveData Android dependency
- Good Coverage with End To End, Integration and Unit Tests
- Material.io guide/components for Ui
- Github Actions CI

## Architecture
- MVVM
- Test Driven Development (TDD)
- Emergent/Evolutionary/Incremental Design
- Patterns applied as code asks, rather than forcing them
- SOLID, DRY, KISS and YAGNI(now)

## Dependency Injection
- Hilt

## Libraries
- Android Jetpack Libraries
  * Room
  * Navigation
  * DataBinding
- Kotlin
  * Flow
  * Coroutine
- Third Party
  * Retrofit
  * GSON
## Tests
- mockito
- Junit
- Espresso
- kotlinx-coroutines-test
- mockwebserver

### More Info
see [scratchpad.txt](scratchpad.txt)
