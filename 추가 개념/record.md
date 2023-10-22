# 🎆 record

## 🎇 등장배경

Java 16버전에서 정식 스펙으로 올라온 기능.

DTO에는 *보일러 플레이트 코드를 불필요하게 크게 작성해야 하는 경우가 많다.

- 보일러 플레이트 코드 : 최소한의 변경으로 여러 곳에서 재사용되면 반복적으로 비슷한 형태를 가지고 있는 코드 ex) get/set 메서드, equals, hashCode, toString 등...

일반적으로 이러한 보일러 플레이트 코드는 lombok 또는 코틀린의 data class 등을 사용하여 중복으로 발생하는 코드의 크기를 줄일 수 있다.

이렇게 비대해진 보일러 플레이트 코드는 자바가 가지고 있는 단점으로 작용한다. 평소 lombok 또는 IDE의 도움으로 코드를 간결하게 작성할 수 있지만, 이는 근본적인 자바가 가지고 있는 한계를 해결한 것이 아니다. 설상가상으로 구글이 kotlin을 공식 지원 언어로 채택하면서 자바의 입지가 크게 흔들리게 되었다. 자바 진영에서는 위기감을 느꼈고 이를 극복하기 위해 업그레이드 하면서 추가된 기능 중 하나가 `record` 이다.


## 🎇 목표

- 객체 지향의 사상에 맞게 데이터를 간결하게 표현하기 위한 방법을 제공
- 개발자가 동작을 확장하는 것보다 불변 데이터를 모델링하는데 집중하도록 함
- 데이터 지향 메서드를 자동으로 구현
- 단, java beans를 대체하기 위한 기술은 아니다. 또한, 어노테이션 지향적인 코드와 거리가 멀다.
- address라는 단순한 주소값을 갖고 있는 녀석 하나 만드는데 보일러 플레이트 코드가 비대해지는 결과를 가지게 된다.


## 🎇 특징

- 구조

```
public record Penguin(String name, String habitat){}
```

record의 구조를 확인해보면, calss 선언 시 record를 사요한다. `레코드명(헤더){바디}` 의 구조를 가지게 된다.

헤더에 나열되는 필드를 `컴포넌트` 라고 부른다.

컴파일러는 헤더를 통해 내부 필드를 추론하는데 이 때 String 타입의 name, habitat이 있다는 것을 인식하게 된다. 이후 코드에 명시적으로 접근자와 생성자, toString, equals, hashCode를 선언하지 않아도 이에 대한 구현을 자동으로 제공해준다.

단, getter 사용 시 getName(), getHabitat() 으로 쓰는게 아니라 필드명, 즉 컴포넌트의 이름만 사요하면 된다. -> name(), habitat()

- 특징

1. record는 불변 객체로 abstract로 선언이 불가하며, 암시적으로 final로 선언된다. 한번 선언 시 setter로 값 변경이 불가하며, 상속이 불가하다.
2. record 내 각 필드는 private final로 정의된다.
3. 다른 클래스를 상속 받는 것이 불가능하지만, 인터페이스를 구현하는 것은 가능하다.
4. 레코드 내부에 멤버 변수를 선언할 수 없다. 그러나 static 변수는 생성이 가능하다. 이는 헤더에 정의한 변수만 record로 관리하기 위해서이다.
5. 위의 4가지 주요 특징을 제외하고 자바의 클래스 개념과 동일하게 사용할 수 있다.
   1. new 키워드를 통해 객체와 가능
   2. static 메소드, static 필드만 선언 가능
   3. 중첩 클래스 사용 가능 및 제너릭 타입으로 지정 가능.

- body 재정의

```
public record Penguin(String name, String habitat) {

    @Override
    public String toString() {
        return "Gundam{" +
                "override_name='" + name + '\'' +
                ", override_habitat='" + habitat + '\'' +
                '}';
    }

    public boolean isFreedom() {
	return this.name.contains("freedom");
    }
}
```

record의 body 부분은 자동으로 생성된 메소드 또는 새로운 메소드를 추가로 작성한다.


- 컴팩트 생성자

컴팩트 생성자는 생성자 매개 변수를 받는 부분이 사라진 형태이다. 개발자가 명시적으로 인스턴스 필드를 초기화 하지 않아도 컴팩트 생성자의 마지막 부분에 초기화 구문이 자동으로 삽입된다. 표준 생성자와 달리 컴팩트 생성자 내부에서는 인스턴스 필드에 접근이 불가하다.

이러한 이유로 컴팩트 생서자에는 컴포넌트로 들어온 값을 불변으로 만들거나 불변식이 만족하는지 유효성 체크 등의 작업을 하기에 적합하다.

컴팩트 생성자의 선언 의도는 생성자 본무에 검증 및 정규호용 코드만 넣어야 한다는 것으로 나머지 초기화 코드는 컴파일러가 자동으로 수행해 개발자는 검증에만 집중이 가능하다.

```
public record Penguin(String name, String habitat){
	public Penguin{
		Objects.requreNonNull(name);
		Objects.requreNonNull(habitat);
	}
}
```

컴팩트 생성자의 사용은 일반 생성자와 동일한 방식으로 사용하면 된다.

`Penguin penguin = new Penguin("뽀로로","남극");`

이처럼 record는 직관적이고, 구조를 단순화게 해준다.


# 🎆 record를 entity로? -> No! DTO로!

record는 entity로 사용이 불가능하다. 

- hibernate와 같은 jpa는 프록시 생성을 위해 인수 생성자, non-final 필드, setter 및 non-final 클래스가 없는 엔티티에 의존한다. 즉, 프록시를 생성하기 위해서 entity는 불변이면 안된다.
- 쿼리 결과를 매핑 시 객체를 인스턴스화 할 수 있도록 매개변수가 없는 생성자가 필요하다. 하지만, record는 매개변수가 없는 생성자(기본 생성자)를 제공하지 않는다.
- 접근자 메소드인 getter가 필수 명명 규칙을 따르지 않는다. record의 getter는 필드명을 그대로 사용한다. 따라서 쿼리 결과 처리 후 수행할 getter, setter에 접근할 수 없다.

# Ref.

> [[Java]자바 record를 entity로?](https://velog.io/@power0080/java%EC%9E%90%EB%B0%94-record%EB%A5%BC-entity%EB%A1%9C)
