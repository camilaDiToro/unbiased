export const ACCESS_DENIED = 600;
export const USER_DISABLED = 601;
export const INVALID_JWT_TOKEN = 602;
export const INVALID_JWT_CLAIM = 603;
export const EXPIRED_JWT_TOKEN = 604;
export const FORBIDDEN = 605;
export const UNAUTHORIZED = 606;
export const INVALID_EMAIL_TOKEN = 607;
export const ALREADY_USED_TOKEN = 608;
export const NOT_EXISTENT_TOKEN = 609;
export const SERVER_ERROR = 500;
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

export const USERFORM_EMAIL_REPEATED = 1000
export const    USERFORM_EMAIL_FORMAT = 1001
export const   USERFORM_PASS_NOTBLAK = 1012

export const    USERPROFILEFORM_USERNAME_REPEATED = 1100
export const    USERPROFILEFORM_USERNAME_LENGTH = 1103
export const    USERPROFILEFORM_MAILOPTIONS_NOTFOUND = 1114

export const    REPORTNEWSFORM_REASON_NOTBLANK = 1202
export const    REPORTNEWSFORM_REASON_LENGTH = 1203
export const    REPORTNEWSFORM_REASON_NOTFOUND = 1204
export const    REPORTNEWSFORM_REASON_NOTNULL = 1205

export const    CREATENEWSFORM_TITLE_NOTBLANK = 1302
export const   CREATENEWSFORM_TITLE_LENGTH = 1303
export const    CREATENEWSFORM_TITLE_NOTNULL = 1305
export const    CREATENEWSFORM_SUBTITLE_NOTBLANK = 1312
export const   CREATENEWSFORM_SUBTITLE_LENGTH = 1313
export const   CREATENEWSFORM_SUBTITLE_NOTNULL = 1315
export const    CREATENEWSFORM_BODY_NOTBLANK = 1322
export const    CREATENEWSFORM_BODY_LENGTH = 1323
export const    CREATENEWSFORM_BODY_NOTNULL = 1325
export const   CREATENEWSFORM_CATEGORIES_NOTFOUND = 1334

export const   COMMENTNEWSFORM_COMMENT_NOTBLANK = 1402
export const   COMMENTNEWSFORM_COMMENT_LENGTH = 1403
export const    COMMENTNEWSFORMM_COMMENT_NOTNULL = 1405

export const    FORM_VALIDATION_ERROR = 1999

const authError = {
  es: "Error de autenticación",
  en: "Authentication error",
};

// eslint-disable-next-line import/no-anonymous-default-export
export default {
  [USERFORM_EMAIL_REPEATED]: {
    es: "El correo ya está en uso",
    en: "The email has already been used"
  },
  [USERFORM_EMAIL_FORMAT]: {
    es: "El correo no es válido",
    en: "The email is not valid"
  },
  [USERFORM_PASS_NOTBLAK]: {
    es: "La contraseña no debe ser vacía",
    en: "The password cannot be blank"
  },
  [USERPROFILEFORM_USERNAME_REPEATED]: {
    es: "El nombre de usuario ya está en uso",
    en: "The username is already in use"
  },
  [USERPROFILEFORM_USERNAME_LENGTH]: {
    es: "El nombre de usuario es demasiado largo",
    en: "The username is too long"
  },
  [USERPROFILEFORM_MAILOPTIONS_NOTFOUND]: {
    es: "La opción seleccionada no existe",
    en: "The selected option does not exist"
  },
  [REPORTNEWSFORM_REASON_NOTBLANK]: {
    es: "Se debe seleccionar una opción de reportado",
    en: "A report option must be selected"
  },
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
  [EXPIRED_JWT_TOKEN]: {es: "Sesión expirada, revalidando sesión",
  en: "Session expired, revalidating session"},
  [FORBIDDEN]: {
    es: "Acceso prohibido",
    en: "Access forbidden",
  },
  [UNAUTHORIZED]: {
    es: "No está authorizado",
    en: "Not authorized",
  },
  [INVALID_EMAIL_TOKEN]: {
    es: "Falla al autenticar correo",
    en: "Failure to authenticate email",
  },
  [ALREADY_USED_TOKEN]: {
    es: "Token de verificación ya utilizado",
    en: "Verification token already used",
  },
  [NOT_EXISTENT_TOKEN]: {
    es: "Token de verificación no existente",
    en: "Verification token non existent",
  },
  [VALIDATION]: {
    es: "Falla de validación del usuario",
    en: "Failure to validate user",
  },
  [SERVER_ERROR]: {
    es: "Error del servidor",
    en: "Server error"
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
