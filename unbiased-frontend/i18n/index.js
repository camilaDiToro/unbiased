import React from "react";
import i18n from "./i18n";
import { I18nextProvider } from "react-i18next";

export const withI18n = (PageComponent) => {
  return (props) => (
      <I18nextProvider i18n={i18n}>
        <PageComponent {...props} />
      </I18nextProvider>
  );
};