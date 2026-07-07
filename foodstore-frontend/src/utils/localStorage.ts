export interface SessionData {
  token: string;
  email: string;
  rol: "ADMIN" | "USUARIO";
}

export const saveSession = (session: SessionData): void => {
  localStorage.setItem("sessionData", JSON.stringify(session));
};

export const getSession = (): SessionData | null => {
  const session = localStorage.getItem("sessionData");

  if (!session) {
    return null;
  }

  return JSON.parse(session) as SessionData;
};

export const getToken = (): string | null => {
  return getSession()?.token ?? null;
};

export const removeSession = (): void => {
  localStorage.removeItem("sessionData");
};