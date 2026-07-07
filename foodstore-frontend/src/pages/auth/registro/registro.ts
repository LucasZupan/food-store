import { register } from "../../../services/auth.service";
import { saveSession } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";
import { showToast } from "../../../utils/toast";

const form = document.getElementById("form") as HTMLFormElement;

const inputNombre = document.getElementById("nombre") as HTMLInputElement;
const inputApellido = document.getElementById("apellido") as HTMLInputElement;
const inputCelular = document.getElementById("celular") as HTMLInputElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputPassword = document.getElementById("password") as HTMLInputElement;

form.addEventListener("submit", async (e: SubmitEvent) => {
  e.preventDefault();

  try {
    const response = await register({
      nombre: inputNombre.value.trim(),
      apellido: inputApellido.value.trim(),
      celular: inputCelular.value.trim(),
      email: inputEmail.value.trim(),
      password: inputPassword.value,
    });

    const token = response.token;
    const payload = JSON.parse(atob(token.split(".")[1]));

    saveSession({
      token,
      email: payload.sub,
      rol: payload.rol,
    });

    showToast("Usuario registrado correctamente", "success");

    navigate("/src/pages/store/home/home.html");
  } catch (error) {
    showToast("Error al registrar usuario", "error");
  }
});