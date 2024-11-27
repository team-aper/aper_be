# aper-library Submodule 전환 안내

`aper-library`는 이제 GitHub Packages 배포 대신 Submodule로 메인 프로젝트에서 사용할 수 있도록 전환되었습니다. 아래는 Submodule 사용 및 설정 방법입니다.

---

## Submodule 추가 및 설정 방법

### 1. Submodule 추가
메인 프로젝트에 `aper-library`를 Submodule로 추가하려면 다음 명령어를 실행
```bash
git submodule add https://github.com/team-aper/aper-library.git libs/aper-library
```

### 2. Submodule 초기화 및 업데이트
Submodule을 초기화하고 업데이트하려면 아래 명령어를 실행
```bash
git submodule init
git submodule update
```

Submodule의 최신 상태를 유지하려면 다음 명령어를 사용

```bash
git submodule update --remote
```

## Gradle 설정
### 1. `settings.gradle` 설정
메인 프로젝트의 `settings.gradle`에 Submodule을 멀티모듈로 등록
```gradle
include ':aper-library'
project(':aper-library').projectDir = file('libs/aper-library')
```
### 2. `build.gradle` 설정
메인 프로젝트의 `build.gradle`에 aper-library를 의존성으로 추가
```gradle
dependencies {
    implementation project(':aper-library')
}
```

## Submodule 사용 시 주의사항
### 1. Submodule 동기화
- 프로젝트를 클론한 후, Submodule을 초기화 및 업데이트
```bash
git submodule update --init --recursive
```

### 2. Submodule 삭제
- Submodule이 필요 없을 경우, 아래 명령어를 실행하여 제거
```bash
git submodule deinit -f libs/aper-library
rm -rf .git/modules/libs/aper-library
git rm -f libs/aper-library
```

### 3. Submodule 고정
- 특정 커밋으로 Submodule 버전을 고정하려면 아래 명령어를 사용
```bash
cd libs/aper-library
git checkout <COMMIT_HASH>
cd ../../
git add libs/aper-library
git commit -m "Fix Submodule to specific commit"
```


