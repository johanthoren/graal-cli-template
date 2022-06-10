#!/usr/bin/env bats

setup() {
    load '../../test_helper/bats-support/load' # this is required by bats-assert!
    load '../../test_helper/bats-assert/load'
}

colored_output() {
    grep '3[0-9]m[0-9]' <<< "$1"
}

### Begin main tests ###

@test "invoking graal-cli-template with no options" {
    run ./graal-cli-template
    assert_success
}

@test "invoking graal-cli-template -h" {
    run ./graal-cli-template -h
    assert_success
}

@test "invoking graal-cli-template -v" {
    run ./graal-cli-template -v
    assert_success
    assert_output --regexp "^[0-9]+\.[0-9]+\.[0-9]+-?(SNAPSHOT)?$"
}

@test "invoking graal-cli-template with the invalid option \"--foo\"" {
    run ./graal-cli-template --foo
    # This is expected to fail with exit code 1.
    assert_failure 1
    assert_output "Unknown option: \"--foo\""
}

### End of main tests ###
