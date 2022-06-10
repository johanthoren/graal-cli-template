#!/usr/bin/env bash

if [ ! $# = 2 ]; then
    echo "Requires 2 arguments: group-id and artifact-id."
    echo "Example: ./initiate.sh xyz.thoren graal-cli-template"
    exit 1
fi

if [ "$(tr -dc '.' <<<"$1" | wc -c)" -ne 1 ]; then
    echo "Example: xyz.thoren"
    exit 1
fi

TLD=$(cut -d'.' -f1 <<<"$1")
DOMAIN=$(cut -d'.' -f2 <<<"$1")
ARTIFACT="$2"
ARTIFACT_NAME="$(tr '-' '_' <<<"$ARTIFACT")"

echo "Initiating the project with the following configuration:"
echo "TLD: $TLD"
echo "Domain: $DOMAIN"
echo "Artifact: $ARTIFACT"

rm -rf .git
mv src/xyz/thoren/graal_cli_template.clj src/xyz/thoren/${ARTIFACT_NAME}.clj
mv test/xyz/thoren/graal_cli_template_test.clj test/xyz/thoren/${ARTIFACT_NAME}_test.clj
mv test/xyz/thoren/graal_cli_template_test.bats test/xyz/thoren/${ARTIFACT_NAME}_test.bats
mv src/xyz/thoren src/xyz/${DOMAIN}
mv test/xyz/thoren test/xyz/${DOMAIN}
mv src/xyz src/${TLD}
mv test/xyz test/${TLD}
find . -type f -exec sed -i '' "s/xyz\.thoren/${TLD}\.${DOMAIN}/g" {} +
find . -type f -exec sed -i '' "s/graal-cli-template/${ARTIFACT}/g" {} +
find .github -type f -exec sed -i '' "s/xyz\/thoren/${TLD}\/${DOMAIN}/g" {} +

echo "Remember to add the following GitHub secrets to your repository:"
echo "SIGN_KEY"
echo "SIGN_KEY_PASSPHRASE"
echo "SIGN_KEY_FINGERPRINT"
