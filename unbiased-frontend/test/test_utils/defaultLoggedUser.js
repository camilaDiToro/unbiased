export function getDefaultLoggedUser (options = {}){
    const loggedUser = {
        authorities: ['ROLE ADMIN', 'ROLE JOURNALIST', 'ROLE OWNER'],
        exp: 1676042196,
        iat: 1676041476,
        imageLink: "http://localhost:8080/webapp_war_exploded/api/users/1/image",
        isAdmin: true,
        iss: "123",
        jti: "6811d6f5-3262-4a39-bac4-a758bf2cc3c4",
        sub: "acaeiro@itba.edu.ar",
        tier: "default",
        tokenType: "refresh",
        userId: 1,
        username: "Aleca"
    }

    return { ...loggedUser, ...options };
}