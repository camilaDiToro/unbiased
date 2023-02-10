import {useTranslation} from "react-i18next";

export const useI18n = () => {
  const {t} = useTranslation()

  return (code, params) => {
    const paramObj = params ? {count: params[0]} : undefined
    return t(code, paramObj);
  };
};


