export const ACCESS_DENIED = 600;
export const USER_DISABLED = 601;
export const INVALID_JWT_TOKEN = 602;
export const INVALID_JWT_CLAIM = 603;
export const EXPIRED_JWT_TOKEN = 604;
export const FORBIDDEN = 605;
export const UNAUTHORIZED = 606;
export const INVALID_EMAIL_TOKEN = 607;

//Validation
export const VALIDATION = 701;
export const INVALID_ROLE = 702;
export const INVALID_PARAMETERS = 703;

//Backend
export const USER_NOT_FOUND = 801;
export const NEWS_NOT_FOUND = 802;
export const COMMENT_NOT_FOUND = 803;
export const IMAGE_NOT_FOUND = 804;

export const INVALID_CATEGORY = 820;
export const INVALID_FILTER = 821;
export const INVALID_ORDER = 822;
export const INVALID_TIME_CONSTRAINT = 823;

export const UNKNOWN = 900;
export const WEB_APP_ERROR = 901;

export const CONN_TIMEOUT = 100;

const authError = {
  es: "Error de autenticación",
  en: "Authentication error",
};

// eslint-disable-next-line import/no-anonymous-default-export
export default {
  [ACCESS_DENIED]: {
    es: "Acceso denegado",
    en: "Access denied",
  },
  [CONN_TIMEOUT]: {
    es: "Falla en la conexion",
    en: "Connection timeout",
  },
  [UNKNOWN]: {
    es: "Error desconocido",
    en: "Unknown error",
  },
  [INVALID_JWT_TOKEN]: authError,
  [INVALID_JWT_CLAIM]: authError,
  [EXPIRED_JWT_TOKEN]: authError,
  [FORBIDDEN]: {
    es: "Acceso prohibido",
    en: "Access forbidden",
  },
  [UNAUTHORIZED]: {
    es: "No está autorizado para realizar la acción",
    en: "Not authorized",
  },
  [INVALID_EMAIL_TOKEN]: {
    es: "Falla al autenticar correo",
    en: "Failure to authenticate email",
  },
  [VALIDATION]: {
    es: "Falla de validación del usuario",
    en: "Failure to validate user",
  },
  [INVALID_ROLE]: {
    es: "Rol inválido",
    en: "Invalid role",
  },
  [INVALID_PARAMETERS]: {
    es: "Parámetros inválidos",
    en: "Invalid parameters",
  },
  [USER_NOT_FOUND]: {
    es: "Usuario no encontrado",
    en: "User not found",
  },
  [NEWS_NOT_FOUND]: {
    es: "Noticia no encontrada",
    en: "News not found",
  },
  [COMMENT_NOT_FOUND]: {
    es: "Comentario no encontrado",
    en: "Coment not found",
  },
  [IMAGE_NOT_FOUND]: {
    es: "Imagen no encontrada",
    en: "Image not found",
  },
  [INVALID_CATEGORY]: {
    es: "Categoría inválida",
    en: "Invalid category",
  },
  [INVALID_FILTER]: {
    es: "Filtro inválida",
    en: "Invalid filter",
  },
  [INVALID_CATEGORY]: {
    es: "Orden inválido",
    en: "Invalid ordery",
  },
  [INVALID_CATEGORY]: {
    es: "Rango de tiempo inválido",
    en: "Invalid time range",
  },
  [WEB_APP_ERROR]: {
    es: "Error de servidor",
    en: "Server error",
  },
};
