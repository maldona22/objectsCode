public interface PersonPred {
    public boolean apply(Person person);
}


class DuplicatePersonPred implements PersonPred {
    Person person;

    public boolean apply(Person person) {
        return !this.person.equals(person);
    }

    DuplicatePersonPred(Person person) {
        this.person = person;
    }
}

class DuplicatePeoplePred implements PersonPred {
    ILoBuddy people;

    public boolean apply(Person person) {
        return !this.people.contains(person);
    }

    DuplicatePeoplePred(ILoBuddy people) {
        this.people = people;
    }
}

class ExtractDuplicatePeoplePred implements PersonPred {
    ILoBuddy people;

    public boolean apply(Person person) {
        return people.contains(person);
    }

    ExtractDuplicatePeoplePred(ILoBuddy people) {
        this.people = people;
    }
}