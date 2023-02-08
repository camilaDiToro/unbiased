const isProd = process.env.ENV === 'prod'

export const baseURL = new URL(isProd ? '/paw-2022b-6' : 'http://localhost:8080/webapp_war_exploded/api')
